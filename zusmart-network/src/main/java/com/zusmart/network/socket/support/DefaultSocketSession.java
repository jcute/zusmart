package com.zusmart.network.socket.support;

import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;

import com.zusmart.network.NetAddress;
import com.zusmart.network.buffer.Buffer;
import com.zusmart.network.buffer.BufferFactory;
import com.zusmart.network.handler.SocketSessionHandlerChain;
import com.zusmart.network.handler.support.DefaultSocketSessionHandlerChain;
import com.zusmart.network.packet.Message;
import com.zusmart.network.packet.MessageProtocol;
import com.zusmart.network.socket.SocketBossEventLoop;
import com.zusmart.network.socket.SocketSession;
import com.zusmart.network.socket.SocketSessionAdapter;
import com.zusmart.network.socket.SocketWorkEventLoop;
import com.zusmart.network.util.CloseUtils;

public class DefaultSocketSession implements SocketSession {

	private final boolean isServerSide;
	private final int readBufferSize;
	private final boolean useDirect;
	private final String socketSessionSequence;
	private final SocketChannel socketChannel;
	private final MessageProtocol messageProtocol;
	private final SocketBossEventLoop socketBossEventLoop;
	private final SocketWorkEventLoop socketWorkEventLoop;
	private final SocketSessionAdapter socketSessionAdapter;
	private final SocketSessionHandlerChain socketSessionHandlerChain;

	private Queue<Buffer> writerBufferQueue;
	private Buffer writerBuffer;
	private Buffer readerBuffer;

	private long registTime;
	private long activeTime;

	private long readerDataLength;
	private long writerDataLength;

	private SelectionKey selectionKey;
	private NetAddress serverAddress;
	private NetAddress clientAddress;

	public DefaultSocketSession(boolean isServerSide, String socketSessionSequence, SocketChannel socketChannel, SocketBossEventLoop socketBossEventLoop, SocketWorkEventLoop socketWorkEventLoop, SocketSessionAdapter socketSessionAdapter, int readBufferSize, boolean useDirect) {
		this.isServerSide = isServerSide;
		this.readBufferSize = readBufferSize;
		this.useDirect = useDirect;
		this.socketSessionSequence = socketSessionSequence;
		this.socketChannel = socketChannel;
		this.socketBossEventLoop = socketBossEventLoop;
		this.socketWorkEventLoop = socketWorkEventLoop;
		this.socketSessionAdapter = socketSessionAdapter;
		this.socketSessionHandlerChain = this.createSocketSessionHandlerChain();
		this.messageProtocol = socketSessionAdapter.createMessageProtocol();
		this.socketSessionAdapter.initSocketSessionHandlerChain(this.socketSessionHandlerChain);
	}

	@Override
	public void write(Message message) {
		if (null == message) {
			return;
		}
		try {
			Buffer buffer = this.messageProtocol.encode(message);
			if (null == buffer) {
				return;
			}
			this.writerBufferQueue.add(buffer);
		} catch (Exception e) {
			this.fireOnException(e);
		}

	}

	@Override
	public void flush() {
		this.socketBossEventLoop.doListener(this, EVENT_WRITABLE);
	}

	@Override
	public void writeAndFlush(Message message) {
		this.write(message);
		this.flush();
	}

	@Override
	public void close() {
		this.socketBossEventLoop.deRegister(this);
	}

	@Override
	public boolean isOpen() {
		return null != this.socketChannel && this.socketChannel.isOpen();
	}

	@Override
	public NetAddress getServerAddress() {
		return this.serverAddress;
	}

	@Override
	public NetAddress getClientAddress() {
		return this.clientAddress;
	}

	// /////////////////////////////////////////////////////////////////////////

	@Override
	public String getSocketSessionSequence() {
		return this.socketSessionSequence;
	}

	@Override
	public SocketBossEventLoop getSocketBossEventLoop() {
		return this.socketBossEventLoop;
	}

	@Override
	public SocketWorkEventLoop getSocketWorkEventLoop() {
		return this.socketWorkEventLoop;
	}

	@Override
	public SocketSessionHandlerChain getSocketSessionHandlerChain() {
		return this.socketSessionHandlerChain;
	}

	@Override
	public long getRegistTime() {
		return this.registTime;
	}

	@Override
	public long getAcitveTime() {
		return this.activeTime;
	}

	@Override
	public long getReaderDataLength() {
		return this.readerDataLength;
	}

	@Override
	public long getWriterDataLength() {
		return this.writerDataLength;
	}

	@Override
	public void fireOnRegister(Selector selector) throws Exception {
		this.socketChannel.configureBlocking(false);
		this.selectionKey = this.socketChannel.register(selector, EVENT_READABLE, this);
		this.initialSocketAddress();
		this.writerBufferQueue = new LinkedList<Buffer>();
		this.writerBuffer = null;
		this.registTime = System.currentTimeMillis();
		this.activeTime = System.currentTimeMillis();
		this.socketWorkEventLoop.submit(new Runnable() {
			@Override
			public void run() {
				socketSessionHandlerChain.fireOnRegister();
			}
		});
	}

	@Override
	public void fireOnReadable() throws Exception {

		if(null == this.readerBuffer){
			this.readerBuffer = BufferFactory.allocate(this.readBufferSize,this.useDirect);
		}
		int limit = -1;
		int total = 0;

		try {
			while ((limit = this.readerBuffer.read(this.socketChannel)) >= 0) {
				if (limit == 0) {
					break;
				}
				total += limit;
			}
			this.activeTime = System.currentTimeMillis();
			this.readerDataLength += total;
		} catch (ClosedChannelException e) {
			this.readerBuffer.release();
			this.readerBuffer = null;
			this.fireUnRegister();
			return;
		} catch (Throwable e) {
			this.readerBuffer.release();
			this.readerBuffer = null;
			this.fireOnException(e);
		}

		if (total > 0) {
			try {
				Buffer buffer = this.readerBuffer.slice();
				final Message message = this.messageProtocol.decode(buffer);
				buffer.release();
				if (null != message) {
					this.readerBuffer.skip(buffer.limit());
					this.socketWorkEventLoop.submit(new Runnable() {
						@Override
						public void run() {
							socketSessionHandlerChain.fireOnMessage(message);
						}
					});
				}
			} catch (Exception e) {
				this.fireOnException(e);
			}

		}
		if (limit < 0) {
			this.fireUnRegister();
		}

	}

	@Override
	public void fireOnWritable() throws Exception {
		if (null == this.writerBuffer) {
			this.writerBuffer = this.writerBufferQueue.poll();
		}
		if (null == this.writerBuffer) {
			this.fireOnListener(EVENT_READABLE);
			return;
		}
		try {
			this.writerBuffer.flip();
			while (this.writerBuffer.hasRemaining()) {
				this.writerDataLength += this.writerBuffer.write(this.socketChannel);
			}
		} catch (ClosedChannelException e) {
			this.fireUnRegister();
			return;
		} catch (Throwable e) {
			this.fireOnException(e);
		}

		this.writerBuffer.release();
		this.writerBuffer = null;
		this.activeTime = System.currentTimeMillis();
		if (this.writerBufferQueue.isEmpty()) {
			this.fireOnListener(EVENT_READABLE);
		} else {
			this.fireOnListener(EVENT_WRITABLE);
		}

	}

	@Override
	public void fireOnListener(int events) throws Exception {
		this.selectionKey.interestOps(events);
	}

	@Override
	public void fireUnRegister() {
		if (this.isOpen()) {
			this.socketWorkEventLoop.submit(new Runnable() {
				@Override
				public void run() {
					socketSessionHandlerChain.fireUnRegister();
				}
			});
		}
		CloseUtils.close(this.selectionKey);
		CloseUtils.close(this.socketChannel);
		this.selectionKey = null;
	}

	@Override
	public void fireOnTimeout() {
		socketWorkEventLoop.submit(new Runnable() {
			@Override
			public void run() {
				socketSessionHandlerChain.fireOnTimeout();
			}
		});
	}

	@Override
	public void fireOnException(final Throwable cause) {
		socketWorkEventLoop.submit(new Runnable() {
			@Override
			public void run() {
				socketSessionHandlerChain.fireOnException(cause);
			}
		});
	}

	@Override
	public String toString() {
		return String.format("SessionID:%s IsOpened:%s [%s -> %s]", this.getSocketSessionSequence(), this.isOpen(), this.isServerSide ? this.serverAddress : this.clientAddress, this.isServerSide ? this.clientAddress : this.serverAddress);
	}

	protected void initialSocketAddress() {
		if (this.isServerSide) {
			this.serverAddress = NetAddress.create((InetSocketAddress) this.socketChannel.socket().getLocalSocketAddress());
			this.clientAddress = NetAddress.create((InetSocketAddress) this.socketChannel.socket().getRemoteSocketAddress());
		} else {
			this.serverAddress = NetAddress.create((InetSocketAddress) this.socketChannel.socket().getRemoteSocketAddress());
			this.clientAddress = NetAddress.create((InetSocketAddress) this.socketChannel.socket().getLocalSocketAddress());
		}
	}

	protected SocketSessionHandlerChain createSocketSessionHandlerChain() {
		return new DefaultSocketSessionHandlerChain(this);
	}

}
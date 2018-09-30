package com.zusmart.network;

import com.zusmart.network.handler.SocketSessionHandlerChain;
import com.zusmart.network.handler.support.DefaultSocketSessionHandler;
import com.zusmart.network.packet.Message;
import com.zusmart.network.packet.MessageProtocol;
import com.zusmart.network.protocol.StringMessage;
import com.zusmart.network.protocol.StringMessageProtocol;
import com.zusmart.network.socket.SocketSession;
import com.zusmart.network.socket.SocketSessionAdapter;
import com.zusmart.network.support.DefaultNetServer;

public class TestHttpServer implements SocketSessionAdapter {

	@Override
	public MessageProtocol createMessageProtocol() {
		return new StringMessageProtocol();
	}

	@Override
	public void initSocketSessionHandlerChain(SocketSessionHandlerChain chain) {
		chain.addLast("test", new DefaultSocketSessionHandler() {

			@Override
			public void onRegister(SocketSession session) {
//				System.err.println("客户端上线:" + session);
			}

			@Override
			public void unRegister(SocketSession session) {
//				System.err.println("客户端下线:" + session);
			}

			@Override
			public void onMessage(SocketSession session, Message message) {
//				System.err.println("服务端收到消息：" + message);
				StringMessage msg = new StringMessage();
				msg.setContent("HTTP/1.1 200 OK\nContent-Length:5\r\n\r\nHello");
				session.writeAndFlush(msg);
			}

			@Override
			public void onTimeout(SocketSession session) {
//				System.err.println("会话超时:" + session);
			}

			@Override
			public void onException(SocketSession session, Throwable cause) {
				cause.printStackTrace();
			}

		});
	}

	public static void main(String[] args) throws Exception {

		NetServerSetting setting = new NetServerSetting();
		// setting.setSessionTimeoutMillis(5000);//设置超时时间为5秒
		NetServer netServer = new DefaultNetServer(NetAddress.create(9080), setting, new TestHttpServer());
		netServer.start();

		System.in.read();

		netServer.close();

	}

}
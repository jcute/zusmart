package com.zusmart.network;

import java.io.File;
import java.io.FileOutputStream;

import com.zusmart.network.handler.SocketSessionHandlerChain;
import com.zusmart.network.handler.support.DefaultSocketSessionHandler;
import com.zusmart.network.packet.Message;
import com.zusmart.network.packet.MessageProtocol;
import com.zusmart.network.packet.support.ByteArrayMessage;
import com.zusmart.network.packet.support.ByteArrayMessageProtocol;
import com.zusmart.network.socket.SocketSession;
import com.zusmart.network.socket.SocketSessionAdapter;
import com.zusmart.network.support.DefaultNetServer;

public class TestFileServer implements SocketSessionAdapter {

	@Override
	public MessageProtocol createMessageProtocol() {
		return new ByteArrayMessageProtocol();
	}

	@Override
	public void initSocketSessionHandlerChain(SocketSessionHandlerChain chain) {
		chain.addLast("monitor", new DefaultSocketSessionHandler() {
			@Override
			public void onRegister(SocketSession session) {
				System.out.println("客户端上线：" + session.getClientAddress());
			}

			@Override
			public void unRegister(SocketSession session) {
				System.out.println("客户端下线：" + session.getClientAddress());
			}

			@Override
			public void onMessage(SocketSession session, Message message) {
				ByteArrayMessage msg = (ByteArrayMessage) message;
				System.out.println("接收文件大小为:" + msg.getContent().length);
				try {
					File file = new File("C:\\Users\\koko\\Desktop\\JSON-A.js");
					if (file.exists()) {
						file.delete();
					}
					FileOutputStream fos = new FileOutputStream(file);
					fos.write(msg.getContent());
					fos.close();
					System.out.println("文件传输完成");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}

	public static void main(String[] args) throws Exception {
		NetServerSetting setting = new NetServerSetting();
		NetServer netServer = new DefaultNetServer(NetAddress.create(9080), setting, new TestFileServer());
		netServer.start();

		System.in.read();

		netServer.close();
	}

}
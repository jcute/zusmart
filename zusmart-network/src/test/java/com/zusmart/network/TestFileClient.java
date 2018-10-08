package com.zusmart.network;

import java.io.File;
import java.io.FileInputStream;

import com.zusmart.network.handler.SocketSessionHandlerChain;
import com.zusmart.network.handler.support.DefaultSocketSessionHandler;
import com.zusmart.network.packet.MessageProtocol;
import com.zusmart.network.packet.support.ByteArrayMessage;
import com.zusmart.network.packet.support.ByteArrayMessageProtocol;
import com.zusmart.network.socket.SocketSession;
import com.zusmart.network.socket.SocketSessionAdapter;
import com.zusmart.network.support.DefaultNetClient;

public class TestFileClient implements SocketSessionAdapter {

	@Override
	public MessageProtocol createMessageProtocol() {
		return new ByteArrayMessageProtocol();
	}

	@Override
	public void initSocketSessionHandlerChain(SocketSessionHandlerChain chain) {
		chain.addLast("test", new DefaultSocketSessionHandler() {
			@Override
			public void unRegister(SocketSession session) {
				System.out.println("un register");
			}

			@Override
			public void onTimeout(SocketSession session) {
				System.out.println("on timeout");
			}

			@Override
			public void onException(SocketSession session, Throwable cause) {
				
			}
		});
	}

	public static void main(String[] args) throws Exception {

		NetClientSetting setting = new NetClientSetting();
		NetClient client = new DefaultNetClient(NetAddress.create("localhost", 9080), setting, new TestFileClient());
		client.start();

		SocketSession session = client.getSocketConnector().getSocketSession();
		System.err.println(session);
		
		ByteArrayMessage msg = new ByteArrayMessage();
		msg.setContent(file2byte("C:\\Users\\koko\\Desktop\\JSON.js"));
		System.out.println(msg.getContent().length);
		session.writeAndFlush(msg);
		
		System.in.read();
		
		session.close();

	}

	public static byte[] file2byte(String path) {
		try {
			FileInputStream in = new FileInputStream(new File(path));
			byte[] data = new byte[in.available()];
			in.read(data);
			in.close();
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
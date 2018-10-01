package com.zusmart.network;

import com.zusmart.network.handler.SocketSessionHandlerChain;
import com.zusmart.network.handler.support.DefaultSocketSessionHandler;
import com.zusmart.network.packet.Message;
import com.zusmart.network.packet.MessageProtocol;
import com.zusmart.network.protocol.StringMessage;
import com.zusmart.network.protocol.StringMessageProtocol;
import com.zusmart.network.socket.SocketSession;
import com.zusmart.network.socket.SocketSessionAdapter;
import com.zusmart.network.support.DefaultNetClient;

public class TestHttpClient implements SocketSessionAdapter{

	@Override
	public MessageProtocol createMessageProtocol() {
		return new StringMessageProtocol();
	}
	
	@Override
	public void initSocketSessionHandlerChain(SocketSessionHandlerChain chain) {
		chain.addLast("demo",new DefaultSocketSessionHandler() {
			@Override
			public void onMessage(SocketSession session, Message message) {
				System.err.println("客户端收到消息："+message);
//				session.close();
			}
		});
	}
	
	public static void main(String[] args) throws Exception {
		
		NetClientSetting setting = new NetClientSetting();
		NetClient client = new DefaultNetClient(NetAddress.create("localhost",9080),setting,new TestHttpClient());
		client.start();
		
		StringMessage msg = new StringMessage();
		msg.setContent("Hello\r\n\r\n");
		
		SocketSession socketSession = client.getSocketConnector().getSocketSession();
		socketSession.writeAndFlush(msg);
		
	}

}

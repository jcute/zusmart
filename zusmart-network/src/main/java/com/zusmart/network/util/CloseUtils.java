package com.zusmart.network.util;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

import com.zusmart.basic.logging.Logger;
import com.zusmart.basic.logging.LoggerFactory;
import com.zusmart.basic.toolkit.Executable;
import com.zusmart.basic.util.StringUtils;

public abstract class CloseUtils{

	private static final Logger logger = LoggerFactory.getLogger(CloseUtils.class);

	public static void close(Executable executable){
		if(null != executable){
			try{
				executable.close();
			}catch(Exception e){
				logger.trace("close executable error : {}",StringUtils.getExceptionMessage(e));
			}
		}
	}

	public static void close(SelectionKey key){
		if(null != key){
			try{
				key.cancel();
			}catch(Exception e){
				logger.trace("close selection key error : {}",StringUtils.getExceptionMessage(e));
			}
		}
	}

	public static void close(Selector selector){
		if(null != selector){
			try{
				selector.close();
			}catch(Exception e){
				logger.trace("close selector error : {}",StringUtils.getExceptionMessage(e));
			}
		}
	}

	public static void close(SocketChannel socketChannel){
		if(null != socketChannel){
			try{
				socketChannel.close();
			}catch(IOException e){
				logger.trace("close channel error : {}",StringUtils.getExceptionMessage(e));
			}
		}
	}

	public static void close(ServerSocketChannel socketChannel){
		if(null != socketChannel){
			try{
				socketChannel.close();
			}catch(IOException e){
				logger.trace("close channel error : {}",StringUtils.getExceptionMessage(e));
			}
		}
	}

	public static void close(ThreadPoolExecutor executor){
		if(null != executor){
			try{
				executor.shutdown();
			}catch(Exception e){
				logger.trace("close executor error : {}",StringUtils.getExceptionMessage(e));
			}
		}
	}

}
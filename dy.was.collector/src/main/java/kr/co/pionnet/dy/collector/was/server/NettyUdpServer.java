package  kr.co.pionnet.dy.collector.was.server;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.pionnet.dependency.io.netty.bootstrap.Bootstrap;
import kr.co.pionnet.dependency.io.netty.channel.Channel;
import kr.co.pionnet.dependency.io.netty.channel.ChannelOption;
import kr.co.pionnet.dependency.io.netty.channel.EventLoopGroup;
import kr.co.pionnet.dependency.io.netty.channel.FixedRecvByteBufAllocator;
import kr.co.pionnet.dependency.io.netty.channel.epoll.EpollChannelOption;
import kr.co.pionnet.dependency.io.netty.channel.nio.NioEventLoopGroup;
import kr.co.pionnet.dependency.io.netty.channel.socket.nio.NioDatagramChannel;
import kr.co.pionnet.dependency.io.netty.handler.logging.LogLevel;
import kr.co.pionnet.dependency.io.netty.handler.logging.LoggingHandler;
import kr.co.pionnet.dy.collector.was.server.handler.NettyUdpServerHandler;

public final class NettyUdpServer implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(NettyUdpServer.class);

	private static NettyUdpServer udpServer;

	private final String hostname;
	private final int port;

	private final NettyUdpServerHandler handler;
	
	public static NettyUdpServer getUdpServer() {
		return udpServer;
	}

	public static void start(String host, Integer port) {
		
		// Don't try to start an already running server
		if (udpServer != null) {
			
			return;
			
		}

		udpServer = new NettyUdpServer(host, port);
		(new Thread(udpServer)).start();
		
	}

	public static void stop() {
		logger.info("Stopping UDP server");
		// Don't try to stop a server that's not running
		if (udpServer == null) {
			return;
		}

		
		
	}

	public NettyUdpServer(final String hostname, final int port) {
		
		
		this.port = port;
		this.hostname = hostname;
		this.handler = new NettyUdpServerHandler();
	}

	public void run() {
		
		Thread.currentThread().setName("udp-server");
		String os = System.getProperty("os.name").toLowerCase();
		logger.debug("os name => {}" ,os);
		
		if (os.startsWith("linux")) {
			logger.info("UDP using Epoll");
			runLinux();
		} else {
			logger.info("UDP using Epoll runGeneric");
			runGeneric();
		}
	}

	private void runGeneric() {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap boot = new Bootstrap();
			boot.channel(NioDatagramChannel.class);
			boot.group(group);
			boot.option(ChannelOption.SO_BROADCAST, false);
			boot.option(ChannelOption.SO_SNDBUF, 16777216);
			boot.option(ChannelOption.SO_RCVBUF, 16777216);
			boot.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(16777216));
			
			boot.handler(new NettyUdpServerHandler());

			InetSocketAddress address = new InetSocketAddress(hostname, port);
		//	boot.bind(address).sync().channel().closeFuture().await();
			
			logger.debug("hostname = >{}",hostname);
			logger.debug("port = >{}",port);
			Channel channel = boot.bind(address).sync().channel();
			channel.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(402400));
			channel.closeFuture().sync();
			

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
	
	private void runLinux() {
		int threadcount = 10;
		 EventLoopGroup group = new NioEventLoopGroup();
		try {
			
			Bootstrap boot = new Bootstrap();
			boot.channel(NioDatagramChannel.class);
			boot.group(group);
			boot.option(ChannelOption.SO_BROADCAST, false);
			boot.option(ChannelOption.SO_BACKLOG, 128);
			 			
			boot.option(ChannelOption.SO_SNDBUF, 1048576);
			boot.option(ChannelOption.SO_RCVBUF, 1048576);
			boot.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(1048576));
			
			boot.option(EpollChannelOption.SO_REUSEPORT, true);
			boot.handler(new LoggingHandler(LogLevel.INFO));
			boot.handler(handler);
			
			
			Channel channel = boot.bind(hostname,port).sync().channel();
			//channel.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(40960));
			channel.closeFuture().await();
			/*for (int i = 0; i < threadcount; ++i) {
				Channel channel = boot.bind(hostname,port).sync().channel();
				channel.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(40960));
				
			}
*/
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
	
}

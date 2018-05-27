package kr.co.pionnet.dy.net.client;

import kr.co.pionnet.dependency.io.netty.bootstrap.Bootstrap;
import kr.co.pionnet.dependency.io.netty.channel.Channel;
import kr.co.pionnet.dependency.io.netty.channel.ChannelOption;
import kr.co.pionnet.dependency.io.netty.channel.EventLoopGroup;
import kr.co.pionnet.dependency.io.netty.channel.FixedRecvByteBufAllocator;
import kr.co.pionnet.dependency.io.netty.channel.SimpleChannelInboundHandler;
import kr.co.pionnet.dependency.io.netty.channel.nio.NioEventLoopGroup;
import kr.co.pionnet.dependency.io.netty.channel.socket.nio.NioDatagramChannel;

public class ReceiveClient implements Runnable{

	private EventLoopGroup group;
	private Channel channel = null;
	
	private BaseClient client;
	private Object handler;
			
	public ReceiveClient(BaseClient client, Object handler) {

		this.client  = client;
		this.handler = handler;
	}
	
	public void run() {
		
		if(group == null && client instanceof UdpClient) {
			initUdpClient((UdpClient)client);
		}

	}
	
	
	private synchronized void initUdpClient(UdpClient udpClient) {

		group = new NioEventLoopGroup();
		try {
			
			
			Bootstrap b = new Bootstrap();
			b.option(ChannelOption.SO_BROADCAST, false);
			b.option(ChannelOption.SO_SNDBUF, 16777216);
			b.option(ChannelOption.SO_RCVBUF, 16777216);
			b.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(1048576));

			b.group(group);
			b.channel(NioDatagramChannel.class);
			b.handler((SimpleChannelInboundHandler)handler);
			channel = b.bind(0).sync().channel();
			
			udpClient.setChannel(channel);
			
			channel.closeFuture().await().await();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
	
	public BaseClient getClient() {
		return client;
	}
	 
	public void shutdown() {	
		
		try {

			if (channel != null ) {
				channel.close();
			}
			
			group.shutdownGracefully();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

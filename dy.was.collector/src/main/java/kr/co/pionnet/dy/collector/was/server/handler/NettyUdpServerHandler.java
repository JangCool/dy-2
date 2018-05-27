package kr.co.pionnet.dy.collector.was.server.handler;

import kr.co.pionnet.dependency.io.netty.channel.ChannelHandlerContext;
import kr.co.pionnet.dependency.io.netty.channel.SimpleChannelInboundHandler;
import kr.co.pionnet.dependency.io.netty.channel.socket.DatagramPacket;

public final class NettyUdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	@Override
	protected void channelRead0(ChannelHandlerContext arg0, DatagramPacket arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}
}

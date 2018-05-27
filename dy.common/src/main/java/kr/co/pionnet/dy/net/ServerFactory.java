package kr.co.pionnet.dy.net;

import kr.co.pionnet.dy.net.server.NetServer;
import kr.co.pionnet.dy.type.NetType;

public abstract class ServerFactory {
	
	private NetServer server;

	public NetServer getUdpServer() {
		return server;
	}
	
	public abstract NetServer createServer(String type, String ip, String port);
	
	public abstract NetServer createServer(NetType type, String ip, String port);

	public abstract NetServer createServer(String type, String ip, int port);
	
	public abstract NetServer createServer(NetType type, String ip, int port);

	public abstract NetServer createServer(String type, String ip, String port,ServerOption option);
	
	public abstract NetServer createServer(NetType type, String ip, String port,ServerOption option);
	
	public abstract NetServer createServer(String type, String ip, int port,ServerOption option);
	
	public abstract NetServer createServer(NetType type, String ip, int port,ServerOption option);

}

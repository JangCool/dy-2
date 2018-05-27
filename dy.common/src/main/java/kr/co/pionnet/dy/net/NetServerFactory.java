package kr.co.pionnet.dy.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kr.co.pionnet.dy.net.server.NetServer;
import kr.co.pionnet.dy.net.server.TCPServerNetty;
import kr.co.pionnet.dy.net.server.UDPServerNetty;
import kr.co.pionnet.dy.type.NetType;

public class NetServerFactory extends ServerFactory {
	
	public final static NetServerFactory factory = new NetServerFactory();
	
	private List servers = Collections.synchronizedList(new ArrayList<NetServer>());

	public static NetServerFactory getInstance() {
		return factory;
	}
	
	@Override
	public NetServer createServer(String type, String ip, String port) {
		return createServer(type, ip, port, null);
	}

	@Override
	public NetServer createServer(NetType type, String ip, String port) {
		return createServer(type, ip, port, null);
	}

	@Override
	public NetServer createServer(String type, String ip, int port) {
		return createServer(type, ip, port, null);
	}

	@Override
	public NetServer createServer(NetType type, String ip, int port) {
		return createServer(type, ip, port, null);

	}

	@Override
	public NetServer createServer(String type, String ip, String port, ServerOption option) {
		return createServer(type, ip, portToInt(port), option);
	}

	@Override
	public NetServer createServer(String type, String ip, int port, ServerOption option) {		
		return createServer(getEnumNetType(type), ip, port, option);
	}
	
	@Override
	public NetServer createServer(NetType type, String ip, String port, ServerOption option) {
		return createServer(type, ip, portToInt(port), option);
	}
	
	@Override
	public NetServer createServer(NetType type, String ip, int port, ServerOption option) {
		
		NetServer server = null;
		
		switch (type) {
		case UDP:
			server = new UDPServerNetty(ip,port,option);
			break;
		case TCP:
			server = new TCPServerNetty(ip,port,option);
			break;
		default:
			break;
		}

		servers.add(server);
		
		return server;
	}
	

	private int portToInt(String port) {
		
		int toIntPort = -1;
		
		if(port != null && port.length() > 0) {
			toIntPort = Integer.parseInt(port);
		}
		
		return toIntPort;
	}
	
	private NetType getEnumNetType(String type ) {
		
		NetType netType = null;
		
		if(NetType.UDP.name().equals(typeToUpperCase(type))) {
			netType = NetType.UDP;
		}
		
		return netType;
	}
	
	private String typeToUpperCase(String type ) {
		
		if(type != null) {
			type = type.toUpperCase();
		}		
		
		return type;
	}




}

package kr.co.pionnet.dy.net.client;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

import kr.co.pionnet.dy.CommonThreadFactory;
import kr.co.pionnet.dy.util.HashUtil;

public class NetClientFactory extends ClientFactory{

	private static LinkedHashMap<Integer, BaseClient> clients = new LinkedHashMap<Integer, BaseClient>();
	
	private static LinkedHashMap<Integer, ReceiveClient> receiveClients = new LinkedHashMap<Integer, ReceiveClient>();
	private static LinkedHashMap<Integer, Thread> receiveThreads = new LinkedHashMap<Integer, Thread>();
	
	private static CommonThreadFactory factory = new CommonThreadFactory("NetClientFactory",true);

	public static int genClientHashKey(ProtocolType type, String ip, int port) {
		return HashUtil.hash(type.name().concat(ip).concat(Integer.toString(port)));
	}
		
	public static synchronized BaseClient createClient(String name, ProtocolType type, String ip, int port, Object handler) {
	
	
		if(type == null || ip == null || port == 0) {
			return null;
		}
		
		int clientHashKey = (name == null) ? genClientHashKey(type, ip, port) : HashUtil.hash(name);
		
		if(clients.containsKey(clientHashKey)) {
			return clients.get(clientHashKey);
		}
		
		
		BaseClient client = null;
		Thread thread = null;
		switch (type) {
		case UDP:
			
			client = new UdpClient(ip,port);
			client.setClientId(clientHashKey);
			ReceiveClient rc = new ReceiveClient(client,handler);
			thread = factory.newThread(rc,"UDP",true);
			thread.start();
			
			receiveClients.put(clientHashKey, rc);
			receiveThreads.put(clientHashKey, thread);
			break;	
		}
		if(client != null ) {
			clients.put(clientHashKey, client);			
		}
		

		return client;
	}
	
	@Override
	public BaseClient createClient(ProtocolType type, String ip, int port) {		
		return createClient(null, type, ip, port,null);
	}

	@Override
	public BaseClient createClient(String name, String type, String ip, int port) {
		
		if(type == null) {
			return null;
		}
		
		ProtocolType protocolType = null;
		if("udp".equals(type.toLowerCase())){
			protocolType = ProtocolType.UDP;
		//}else if("tcp".equals(type.toLowerCase())){
		//	protocolType = ProtocolType.TCP;
		}else {
			
			System.out.println("[ "+type+" ] protocol is not supported.");//이 프로토콜을 지원하지 않습니다.
			return null;
		}
		
		if(name == null) {
			return createClient(null,protocolType, ip, port,null);
		}

		return createClient(name,protocolType, ip, port,null);
	}


	@Override
	BaseClient createClient(String type, String ip, int port) {
		return createClient(null,type, ip, port);
	}
	
	public static BaseClient getClient(String key) {
		return clients.get(HashUtil.hash(key));
	}
	

	
	public static void interrupt() {
		factory.getThreadGroup().interrupt();
	}
	
	public static void destory() {
		factory.destory();
	}
	
	public static void removeClient(String key) {
		
		int hashkey = HashUtil.hash(key);
		synchronized (clients) {
			
			Thread t = receiveThreads.get(hashkey);
			if(t != null) {
				t.interrupt();	
			}
			ReceiveClient rc = receiveClients.get(hashkey);
		
			if(rc != null) {
				rc.shutdown();
			}
			
			clients.remove(hashkey);
		}		
	}
	
	public static void shutdown() {

		Collection c = receiveClients.values();
		Iterator<ReceiveClient> itr = c.iterator();
		
		while (itr.hasNext()){
			itr.next().shutdown();
		}
		
	}

}

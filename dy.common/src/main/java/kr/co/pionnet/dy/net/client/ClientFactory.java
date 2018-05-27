package kr.co.pionnet.dy.net.client;

public abstract class ClientFactory {

	abstract BaseClient createClient(ProtocolType type, String ip, int port);
	
	abstract BaseClient createClient(String type, String ip, int port);

	abstract BaseClient createClient(String name, String type, String ip, int port);
}

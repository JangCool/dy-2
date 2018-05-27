package kr.co.pionnet.dy.net.client;

import java.net.InetAddress;

public abstract class BaseClient  {
	
	private String ip;
	private int port;
	private boolean close;
	private int clientId;

	public BaseClient() {}
	
	public BaseClient(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	public boolean isClose() {
		return close;
	}

	public void setClose(boolean close) {
		this.close = close;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}
	
	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	abstract void setClient();
	
	public abstract boolean send(byte[] bytes);
	
	public abstract boolean send(String msg);

	public abstract boolean send(NetDataType netDataType, byte[] bytes);

	public abstract boolean sendPACK(byte[] bytes);

	public abstract boolean sendJSON(byte[] bytes);

	public abstract boolean sendMTU(String msg);
	
	public abstract boolean sendMTU(byte[] bytes);
	
	public abstract boolean sendMTU(Object object);

	public abstract boolean sendMTU(NetDataType netDataType, byte[] bytes);
		
	public abstract void close();

}

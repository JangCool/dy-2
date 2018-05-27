package kr.co.pionnet.dy.net.server;

public abstract class UDPServer implements NetServer{

	private NetServer server;
	
	@Override
	public abstract void start();
	

	@Override
	public abstract void stop();

	
	@Override
	public NetServer getServer() {
		return server;
	}

}

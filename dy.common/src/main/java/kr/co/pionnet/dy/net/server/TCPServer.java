package kr.co.pionnet.dy.net.server;

public abstract class TCPServer implements NetServer{

	@Override
	public abstract void start();

	
	@Override
	public abstract void stop();


	@Override
	public NetServer getServer() {
		// TODO Auto-generated method stub
		return null;
	}

}

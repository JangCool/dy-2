package kr.co.pionnet.dy.net.client;

public interface ResponseHandler {
	
	public void receive(BaseClient client, byte[] res) throws Exception;
	
	public void exceptionCaught(BaseClient client, byte[] responseData, Throwable e);
}

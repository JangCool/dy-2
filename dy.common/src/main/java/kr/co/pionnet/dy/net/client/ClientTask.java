package kr.co.pionnet.dy.net.client;

class ClientTask implements Runnable {
	
    private BaseClient client;
    private ResponseHandler handler;
    private byte[] responseData;
    
    public ClientTask(BaseClient client,byte[] responseData, ResponseHandler handler) { 
    	this.client = client; 
    	this.handler = handler;
    	this.responseData = responseData;
    }
    
    public void run() {
    	try {			
    		handler.receive(client, this.responseData);
		} catch (Throwable e) {
			handler.exceptionCaught(client, this.responseData,e);
		}
    }
 }
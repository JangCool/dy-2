package kr.co.pionnet.dy.vo;

public class Data {
	public long txid;
	public long eTime;	
	public int elapsed;
	public byte[] data;
	
	
	public Data(long txid, long eTime, int elapsed, byte[] data) {
		
		this.txid = txid;
		this.eTime = eTime;
		this.elapsed = elapsed;
		this.data = data;
	}
	
}
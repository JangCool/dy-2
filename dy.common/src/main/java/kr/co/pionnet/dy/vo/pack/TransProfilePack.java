package kr.co.pionnet.dy.vo.pack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TransProfilePack extends Pack {

	public long txid;	
	public long eTime;
	public int elapsed;
	public byte[] bytes;
	public int payLoadLength;


	@Override
	TransProfilePack read(DataInputStream unPacker) {

		try {
	
			this.txid = unPacker.readLong();
			this.eTime = unPacker.readLong();
			this.elapsed = unPacker.readInt();
			this.payLoadLength = unPacker.readInt();

			byte[] b = new byte[payLoadLength];
			unPacker.readFully(b);
			this.bytes = b;

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return this;
		
	}


	@Override
	void write(DataOutputStream packer) {
		
		try {
			packer.writeLong(txid);
			packer.writeLong(eTime);
			packer.writeInt(elapsed);
			packer.writeInt(payLoadLength);
			packer.write(bytes);			
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}
	
	
}

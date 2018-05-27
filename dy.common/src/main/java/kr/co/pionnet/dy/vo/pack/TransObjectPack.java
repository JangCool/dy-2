package kr.co.pionnet.dy.vo.pack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TransObjectPack extends Pack {

	public int payLoadLength;
	public byte[] bytes;

	@Override
	TransObjectPack read(DataInputStream unPacker) {

		try {
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
			packer.writeInt(payLoadLength);			
			packer.write(bytes);		
			
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}
	
	
}

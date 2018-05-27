package kr.co.pionnet.dy.vo.pack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import kr.co.pionnet.dy.util.TextUtil;

public class TransTextPack extends Pack {
	
	public int hash;
	public int hash2;
    public String text;
    public String text2;
	
	
	@Override
	Pack read(DataInputStream unPacker) {
		
		try {
			
			this.hash 		= (int) 	unPacker.readInt();
			this.hash2	 	= (int) 	unPacker.readInt();
			this.text 		= 			unPacker.readUTF();
			this.text2 		= 			unPacker.readUTF();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	@Override
	void write(DataOutputStream packer) {

		try {
			
			packer.writeInt(hash);
			packer.writeInt(hash2);
			packer.writeUTF(TextUtil.nvl(text, ""));
			packer.writeUTF(TextUtil.nvl(text2, ""));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}


	
}

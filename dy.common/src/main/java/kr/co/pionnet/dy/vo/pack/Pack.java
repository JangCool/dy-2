package kr.co.pionnet.dy.vo.pack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public abstract class Pack {
	
	public byte dataType; // ※ 필수 
		
	public Pack unPack(byte[] bytes) {
		
		Pack pack = null;
		ByteArrayInputStream bais = null;
		DataInputStream unPacker = null;
		try {
			bais = new ByteArrayInputStream(bytes);	
			unPacker = new DataInputStream(bais);

			this.dataType = unPacker.readByte();
			pack = read(unPacker);
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			
			try {
				if(unPacker != null) {
					unPacker.close();					
				}
				
				if(bais != null) {
					bais.close();					
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pack;
	}
	
	public byte[] toByteArray() {

		ByteArrayOutputStream baos = null;
		DataOutputStream packer = null;
		
		try {
			
			baos = new ByteArrayOutputStream();
			packer = new DataOutputStream(baos);
			
			packer.write(dataType);
			write(packer);
			
			byte[] byteArray = baos.toByteArray();

			return byteArray;
		
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			
			try {
				if(packer != null) {
					packer.close();					
				}
				
				if(baos != null) {
					baos.close();					
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	abstract Pack read(DataInputStream unPacker);
	
	abstract void write(DataOutputStream packer);
}

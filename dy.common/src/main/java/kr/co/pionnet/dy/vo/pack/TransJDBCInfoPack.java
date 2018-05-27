package kr.co.pionnet.dy.vo.pack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TransJDBCInfoPack extends Pack {

	public int hash;
    public String url;
    public String userName;
    public String driverName;
    public String driverVersion;
	
	
	@Override
	Pack read(DataInputStream unPacker) {
		try {
			
			this.hash = (int) unPacker.readInt();
			this.url = unPacker.readUTF();
			this.userName = unPacker.readUTF();
			this.driverName = unPacker.readUTF();
			this.driverVersion = unPacker.readUTF();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	@Override
	void write(DataOutputStream packer) {

		try {
			
			packer.writeInt(hash);
			packer.writeUTF(url);
			packer.writeUTF(userName);
			packer.writeUTF(driverName);
			packer.writeUTF(driverVersion);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}


	
}

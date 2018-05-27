package kr.co.pionnet.dy.vo.pack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import kr.co.pionnet.dy.util.TextUtil;

public class TransAgentInfoPack extends Pack {
	public String aid;
	public String hostip;
	public String serverType;
	public byte isLive;  //1  or 0 : 
	public int port;
	public int changePort;
	public byte java_vendor;
	public String java_ver;
	
	@Override
	Pack read(DataInputStream unPacker) {
		try {
			
			this.aid = unPacker.readUTF();
			this.hostip = unPacker.readUTF();
			this.serverType = unPacker.readUTF();
			this.isLive = unPacker.readByte();
			this.port = unPacker.readInt();
			this.changePort = unPacker.readInt();
			this.java_vendor = unPacker.readByte();
			this.java_ver = unPacker.readUTF();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	@Override
	void write(DataOutputStream packer) {

		try {
			
			packer.writeUTF(TextUtil.nvl(aid, ""));
			packer.writeUTF(TextUtil.nvl(hostip, ""));
			packer.writeUTF(TextUtil.nvl(serverType, ""));
			packer.writeByte(isLive);
			packer.writeInt(port);
			packer.writeInt(changePort);
			packer.writeByte(java_vendor);
			packer.writeUTF(java_ver);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}


	
}

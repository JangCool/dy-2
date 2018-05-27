package kr.co.pionnet.dy.vo.pack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import kr.co.pionnet.dy.util.TextUtil;

public class TransErrorPack extends Pack {

	public int serv_h;
	public int hash;
    public int clazz_h;
	public byte type;
    public String text;
    public String clazz;
    public String pkg;
    public String aid;
	public int app;
	public int method;
	public int jdbc;
	public int external;
	public int total;
	public long end_t;
	public boolean isRoot;

	
	@Override
	Pack read(DataInputStream unPacker) {
		
		try {
			this.serv_h 			= unPacker.readInt();
			this.hash 				= unPacker.readInt();
			this.clazz_h 			= unPacker.readInt();
			this.type 				= unPacker.readByte();
			this.text 				= unPacker.readUTF();
			this.clazz 				= unPacker.readUTF();
			this.pkg 				= unPacker.readUTF();
			this.aid 				= unPacker.readUTF();
			this.app 				= unPacker.readInt();
			this.method 			= unPacker.readInt();
			this.jdbc 				= unPacker.readInt();
			this.external			= unPacker.readInt();
			this.total 				= unPacker.readInt();
			this.end_t 				= unPacker.readLong();
			this.isRoot 			= unPacker.readBoolean();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return this;
		
	}

	@Override
	void write(DataOutputStream packer) {
		
		try {
			
			packer.writeInt(serv_h);
			packer.writeInt(hash);
			packer.writeInt(clazz_h);
			packer.writeByte(type);
			packer.writeUTF(TextUtil.nvl(text, ""));
			packer.writeUTF(TextUtil.nvl(clazz, ""));
			packer.writeUTF(TextUtil.nvl(pkg, ""));
			packer.writeUTF(TextUtil.nvl(aid, ""));
			packer.writeInt(app);
			packer.writeInt(method);
			packer.writeInt(jdbc);
			packer.writeInt(external);
			packer.writeInt(total);
			packer.writeLong(end_t);
			packer.writeBoolean(isRoot);		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}

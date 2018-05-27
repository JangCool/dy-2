package kr.co.pionnet.dy.vo.pack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import kr.co.pionnet.dy.type.DataType;
import kr.co.pionnet.dy.util.TextUtil;

public class ProfilePack extends Pack {

	public long sTime;
	public long eTime;
	public int elapsed;
	public int hash; 
	public int sql_h; 
	public int level;
	public byte type;
	public byte sqlType;
	public byte jdbcType;	
	public int jdbc_i_h;
	public String params;
	public String paramTypes;	
	public short updateCount;
	public int fetch_c;	
	public String externalApi;
	public int step;
	public int e;
	public byte e_g;
	
	public ProfilePack() {
		this.dataType = DataType.PROFILE;
	}

	@Override
	Pack read(DataInputStream unPacker) {
		
		try {
			
			this.sTime 			= unPacker.readLong();
			this.eTime 			= unPacker.readLong();
			this.elapsed 		= unPacker.readInt();
			this.hash 			= unPacker.readInt();
			this.sql_h 			= unPacker.readInt();
			this.level 			= unPacker.readInt();
			this.type 			= unPacker.readByte();
			this.sqlType 		= unPacker.readByte();
			this.jdbcType 		= unPacker.readByte();
			this.jdbc_i_h 		= unPacker.readInt();
			this.params 		= unPacker.readUTF();
			this.paramTypes 	= unPacker.readUTF();
			this.updateCount 	= unPacker.readShort();
			this.fetch_c 		= unPacker.readInt();
			this.externalApi 	= unPacker.readUTF();
			this.step 			= unPacker.readInt();
			this.e 				= unPacker.readInt();
			this.e_g 			= unPacker.readByte();
							
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return this;
	}



	@Override
	void write(DataOutputStream packer) {
		
		try {
			
			packer.writeLong(sTime);
			packer.writeLong(eTime);
			packer.writeInt(elapsed);
			packer.writeInt(hash);
			packer.writeInt(sql_h);
			packer.writeInt(level);
			packer.writeByte(type);
			packer.writeByte(sqlType);
			packer.writeByte(jdbcType);
			packer.writeInt(jdbc_i_h);
			packer.writeUTF(TextUtil.nvl(params, ""));
			packer.writeUTF(TextUtil.nvl(paramTypes, ""));
			packer.writeShort(updateCount);
			packer.writeInt(fetch_c);
			packer.writeUTF(TextUtil.nvl(externalApi, ""));
			packer.writeInt(step);
			packer.writeInt(e);
			packer.writeByte(e_g);
						
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	

}

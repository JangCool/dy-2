package kr.co.pionnet.dy.vo.pack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.plaf.synth.SynthSplitPaneUI;

import kr.co.pionnet.dy.util.TextUtil;

public class TransXviewPack extends Pack {

	public String aid;
	public short gid;
	public long txid;
	public long sTime;
	public long eTime;
	public int elapsed;
	public int serv_h;
    public int uagent_h;
    public int qst_h;    
	public int sql_t;
	public int sql_c;
	public int fetch_c;
	public int cpu_t;
	public int bytes;
	public String l_ip;
	public String r_ip;
	public String method;
	public int ref_h;	
	public short e_type;
	public int e;
	public int e_cls;
	public byte e_g;
	public int uid;



	@Override
	TransXviewPack read(DataInputStream unPacker) {

		try {
		
			this.aid 			=			unPacker.readUTF();
			this.gid 			= 			unPacker.readShort();
			this.txid 			= 			unPacker.readLong();
			this.sTime 			= 			unPacker.readLong();
			this.eTime 			= 			unPacker.readLong();
			this.elapsed 		= 			unPacker.readInt();
			this.serv_h			= 		 	unPacker.readInt();
			this.uagent_h 		= 		 	unPacker.readInt();
			this.qst_h 			= 		 	unPacker.readInt();
			this.sql_t 			= 		 	unPacker.readInt();
			this.sql_c 			= 		 	unPacker.readInt();
			this.fetch_c 		= 		 	unPacker.readInt();
			this.cpu_t 			= 			unPacker.readInt();		
			this.l_ip 			= 			unPacker.readUTF();
			this.r_ip 			= 			unPacker.readUTF();
			this.method 		= 			unPacker.readUTF();	
			this.ref_h 			= 		 	unPacker.readInt();
			this.e_type 		= (short) 	unPacker.readShort();
			this.e 				= (int) 	unPacker.readInt();
			this.e_cls 			= (int)		unPacker.readInt();
			this.e_g 			=			unPacker.readByte();
			this.uid 			= (int)		unPacker.readInt();
//		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return this;
		
	}


	@Override
	void write(DataOutputStream packer) {
		
		try {
			packer.writeUTF(aid);			
			packer.writeShort(gid);
			packer.writeLong(txid);
			packer.writeLong(sTime);
			packer.writeLong(eTime);
			packer.writeInt(elapsed);
			packer.writeInt(serv_h);
			packer.writeInt(uagent_h);
			packer.writeInt(qst_h);
			packer.writeInt(sql_t);
			packer.writeInt(sql_c);
			packer.writeInt(fetch_c);
			packer.writeInt(cpu_t);			
			packer.writeUTF(TextUtil.nvl(l_ip, ""));
			packer.writeUTF(TextUtil.nvl(r_ip, ""));
			packer.writeUTF(TextUtil.nvl(method, ""));
			packer.writeInt(ref_h);
			packer.writeShort(e_type);
			packer.writeInt(e);
			packer.writeInt(e_cls);
			packer.writeByte(e_g);
			packer.writeInt(uid);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}
	
	
}

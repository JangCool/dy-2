package kr.co.pionnet.dragon.db;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import kr.co.pionnet.dy.common.DataInputTypeConverter;

public class DataObjInfo  {
	
	
	long pos;
	int size;
	
	public DataObjInfo() {
		
	}
	public DataObjInfo(  int size, int pos) {
		
		this.size = size;
		this.pos = pos;
		
	}
	
	public ByteArrayOutputStream writeStream(ByteArrayOutputStream baos) throws IOException {
		DataOutputStream dop = new DataOutputStream(baos);
		dop.writeInt(size);
		dop.writeLong(pos);
		return baos;
	}
	
	public DataObjInfo readStream(DataInputStream dip) throws IOException {
		DataInputTypeConverter ditc = new DataInputTypeConverter(dip);
		/*this.etime =  ditc.readDecimal();
		this.pos =  ditc.readDecimal();
		this.objectSize = (int) ditc.readDecimal();*/
		
		
		this.size = dip.readInt();
		this.pos = dip.readLong();
		
		return this;
		
	}
	
	public void readFields(DataInputTypeConverter dip) throws IOException {
		// TODO Auto-generated method stub
		/*this.etime =  dip.readDecimal();
		this.pos =  dip.readDecimal();
		this.objectSize = (int) dip.readDecimal();
		*/
	}

	
	public DataObjInfo read(DataInputTypeConverter dip) throws IOException {
		// TODO Auto-generated method stub
		DataObjInfo w = new DataObjInfo();
		w.readFields(dip);
		
		return w;
	}
	
	public void print() {
		
	}
	
}


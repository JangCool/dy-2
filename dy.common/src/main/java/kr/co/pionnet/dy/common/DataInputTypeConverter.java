package kr.co.pionnet.dy.common;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

import kr.co.pionnet.dy.type.DataType;

public class DataInputTypeConverter  {

	
	private static DataInputStream di = null;
	
	public final static int INT3_MIN_VALUE = 0xff800000;
	public final static int INT3_MAX_VALUE = 0x007fffff;
	public final static long LONG5_MIN_VALUE = 0xffffff8000000000L;
	public final static long LONG5_MAX_VALUE = 0x0000007fffffffffL;
	
	public DataInputTypeConverter(byte[] buff) {
		this(new ByteArrayInputStream(buff));
	}  
	public DataInputTypeConverter(ByteArrayInputStream bais) {		
		di = new DataInputStream(bais);
	}
	
	public DataInputTypeConverter(DataInputStream dis) {		
		di = dis;
	}
	
	public byte[] read(int len) throws IOException {
		
		byte[] buff = new byte[len];
		di.readFully(buff);
		return buff;
	}

	public byte[] readShortBytes() throws IOException {
		int len = readUnsignedShort();		
		byte[] buff = new byte[len];
		di.readFully(buff);
		return buff;
	}
	
	public int readInt3() throws IOException {
		byte[] readBuffer = read(3);
		return toInt3(readBuffer, 0);
	}

	public long readLong5() throws IOException {
		byte[] readBuffer =read(5);
		return toLong5(readBuffer, 0);
	}

	public long readDecimal() throws IOException {
		byte len = readByte();
		switch (len) {
		case 0:
			return 0;
		case 1:
			return readByte();
		case 2:
			return readShort();
		case 3:
			return readInt3();
		case 4:
			return readInt();
		case 5:
			return readLong5();
		default:
			return readLong();
		}
	}

	
	public String readText() throws IOException {		
		byte[] buffer = readBlob();
		return new String(buffer, "UTF8");

	}

	public static boolean toBoolean(byte[] buf, int pos) {
		return buf[pos]!=0;	
	}
	
	public static short toShort(byte[] buf, int pos) {
		int ch1 = buf[pos] & 0xff;
		int ch2 = buf[pos + 1] & 0xff;
		return (short) ((ch1 << 8) + (ch2 << 0));
	}

	public static int toInt3(byte[] buf, int pos) {
		int ch1 = buf[pos] & 0xff;
		int ch2 = buf[pos + 1] & 0xff;
		int ch3 = buf[pos + 2] & 0xff;

		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8)) >> 8;
	}
	public static int toInt(byte[] buf, int pos) {
		int ch1 = buf[pos] & 0xff;
		int ch2 = buf[pos + 1] & 0xff;
		int ch3 = buf[pos + 2] & 0xff;
		int ch4 = buf[pos + 3] & 0xff;
		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}

	public static long toLong(byte[] buf, int pos) {
		return (((long) buf[pos] << 56)//
				+ ((long) (buf[pos + 1] & 255) << 48) //
				+ ((long) (buf[pos + 2] & 255) << 40) //
				+ ((long) (buf[pos + 3] & 255) << 32) //
				+ ((long) (buf[pos + 4] & 255) << 24) + ((buf[pos + 5] & 255) << 16) //
				+ ((buf[pos + 6] & 255) << 8) //
		+ ((buf[pos + 7] & 255) << 0));
	}

	public static long toLong5(byte[] buf, int pos) {
		return (((long) buf[pos] << 32) + //
				((long) (buf[pos + 1] & 255) << 24) + //
				((buf[pos + 2] & 255) << 16) + //
				((buf[pos + 3] & 255) << 8) + //
		((buf[pos + 4] & 255) << 0));
	}

	public static float toFloat(byte[] buf, int pos) {
		return Float.intBitsToFloat(toInt(buf, pos));
	}

	public static double toDouble(byte[] buf, int pos) {
		return Double.longBitsToDouble(toLong(buf, pos));
	}

	public static byte[] get(byte[] buf, int pos, int length) {
		byte[] out = new byte[length];
		System.arraycopy(buf, pos, out, 0, length);
		return out;
	}

	public int[] readDecimalArray(int[] data) throws IOException {
		int length = (int) readDecimal();
		data = new int[length];
		for (int i = 0; i < length; i++) {
			data[i] = (int) readDecimal();
		}
		return data;
	}

	public long[] readDecimalArray() throws IOException {
		int length = (int) readDecimal();
		long[] data = new long[length];
		for (int i = 0; i < length; i++) {
			data[i] = readDecimal();
		}
		return data;
	}

	public long[] readArray() throws IOException {
		return readArray(new long[0]);
	}

	public long[] readArray(long[] data) throws IOException {
		int length = readShort();
		data = new long[length];
		for (int i = 0; i < length; i++) {
			data[i] = readLong();
		}
		return data;
	}

	public int[] readArray(int[] data) throws IOException {
		int length = readShort();
		data = new int[length];
		for (int i = 0; i < length; i++) {
			data[i] = readInt();
		}
		return data;
	}

	public float[] readArray(float[] data) throws IOException {
		int length = readShort();
		data = new float[length];
		for (int i = 0; i < length; i++) {
			data[i] = readFloat();
		}
		return data;
	}


	
	public void readFully(byte[] b) throws IOException {
		
		di.readFully(b);
	}

	public void readFully(byte[] b, int off, int len) throws IOException {		
		di.readFully(b, off, len);
	}

	public int skipBytes(int n) throws IOException {
		
		return di.skipBytes(n);
	}

	public boolean readBoolean() throws IOException {
		
		return di.readBoolean();
	}

	public byte readByte() throws IOException {
		
		return di.readByte();
	}

	public int readUnsignedByte() throws IOException {
		
		return di.readUnsignedByte();
	}

	public short readShort() throws IOException {
		
		return di.readShort();
	}

	public int readUnsignedShort() throws IOException {
		
		return di.readUnsignedShort();
	}

	public char readChar() throws IOException {
		
		return di.readChar();
	}

	public int readInt() throws IOException {
		
		return di.readInt();
	}

	public long readLong() throws IOException {
		
		return di.readLong();
	}

	public float readFloat() throws IOException {
		
		return di.readFloat();
	}

	public double readDouble() throws IOException {
		
		return di.readDouble();
	}

	public byte[] readBlob() throws IOException {
		int baselen = readUnsignedByte();
		switch (baselen) {
		case 255: {
			int len = readUnsignedShort();
			byte[] buffer = read(len);
			return buffer;
		}
		case 254: {
			int len = this.readInt();
			byte[] buffer = read(len);
			return buffer;
		}
		case 0: {
			return new byte[0];
		}
		default:
			byte[] buffer = read(baselen);
			return buffer;
		}
	}
	
	public int available() throws IOException {
		return this.di == null ? 0 : this.di.available();
	}
	
	
/*	public ITransBag readBag() throws IOException {
		byte dataType = di.readByte();
		System.out.println("#### dataType : "+ dataType);
		ITransBag b = null;
		switch (dataType) {
		case DataType.TRACKER:
			b = (new TransXviewBag()).read(this); 
			
			
			break;

		default:
			break;
		}
		return b;
	}
	*/
}

package kr.co.pionnet.dy.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

public class DataOutputTypeConverter  {

	
	private  DataOutputStream dot = null;
	private ByteArrayOutputStream baos = null;
	 
	
	public final static int INT3_MIN_VALUE = 0xff800000;
	public final static int INT3_MAX_VALUE = 0x007fffff;
	public final static long LONG5_MIN_VALUE = 0xffffff8000000000L;
	public final static long LONG5_MAX_VALUE = 0x0000007fffffffffL;
	
	public DataOutputTypeConverter(ByteArrayOutputStream baos) {
		this.baos = baos;
		dot = new DataOutputStream(baos);
	}
	
	public DataOutputTypeConverter(DataOutputStream out) {
		this.dot = out;
	}
	
	public  void writeDecimal(long v) throws IOException {
		if (v == 0) {
			dot.writeByte(0);
		} else if (Byte.MIN_VALUE <= v && v <= Byte.MAX_VALUE) {
			byte[] b = new byte[2];
			b[0] = 1;
			b[1] = (byte) v;
			dot.write(b);
		} else if (Short.MIN_VALUE <= v && v <= Short.MAX_VALUE) {
			byte[] b = new byte[3];
			b[0] = 2;
			toBytes(b, 1, (short) v);
			dot.write(b);
		} else if (INT3_MIN_VALUE <= v && v <= INT3_MAX_VALUE) {
			byte[] b = new byte[4];
			b[0] = 3;
			dot.write(toBytes3(b, 1, (int) v), 0, 4);
		} else if (Integer.MIN_VALUE <= v && v <= Integer.MAX_VALUE) {
			byte[] b = new byte[5];
			b[0] = 4;
			dot.write(toBytes(b, 1, (int) v), 0, 5);
		} else if (LONG5_MIN_VALUE <= v && v <= LONG5_MAX_VALUE) {
			byte[] b = new byte[6];
			b[0] = 5;
			dot.write(toBytes5(b, 1, v), 0, 6);
		} else if (Long.MIN_VALUE <= v && v <= Long.MAX_VALUE) {
			byte[] b = new byte[9];
			b[0] = 8;
			dot.write(toBytes(b, 1, v), 0, 9);
		}		
	}
	
	
	
	public static void writeDecimal(long v, DataOutputStream dot) throws IOException {
		if (v == 0) {
			dot.writeByte(0);
		} else if (Byte.MIN_VALUE <= v && v <= Byte.MAX_VALUE) {
			byte[] b = new byte[2];
			b[0] = 1;
			b[1] = (byte) v;
			dot.write(b);
		} else if (Short.MIN_VALUE <= v && v <= Short.MAX_VALUE) {
			byte[] b = new byte[3];
			b[0] = 2;
			toBytes(b, 1, (short) v);
			dot.write(b);
		} else if (INT3_MIN_VALUE <= v && v <= INT3_MAX_VALUE) {
			byte[] b = new byte[4];
			b[0] = 3;
			dot.write(toBytes3(b, 1, (int) v), 0, 4);
		} else if (Integer.MIN_VALUE <= v && v <= Integer.MAX_VALUE) {
			byte[] b = new byte[5];
			b[0] = 4;
			dot.write(toBytes(b, 1, (int) v), 0, 5);
		} else if (LONG5_MIN_VALUE <= v && v <= LONG5_MAX_VALUE) {
			byte[] b = new byte[6];
			b[0] = 5;
			dot.write(toBytes5(b, 1, v), 0, 6);
		} else if (Long.MIN_VALUE <= v && v <= Long.MAX_VALUE) {
			byte[] b = new byte[9];
			b[0] = 8;
			dot.write(toBytes(b, 1, v), 0, 9);
		}		
	}
	
	public static byte[] toBytes(short v) {
		byte buf[] = new byte[2];
		buf[0] = (byte) ((v >>> 8) & 0xFF);
		buf[1] = (byte) ((v >>> 0) & 0xFF);
		return buf;
	}

	public static byte[] toBytes(byte[] buf, int off, short v) {
		buf[off] = (byte) ((v >>> 8) & 0xFF);
		buf[off + 1] = (byte) ((v >>> 0) & 0xFF);
		return buf;
	}

	public static byte[] toBytes(int v) {
		byte buf[] = new byte[4];
		buf[0] = (byte) ((v >>> 24) & 0xFF);
		buf[1] = (byte) ((v >>> 16) & 0xFF);
		buf[2] = (byte) ((v >>> 8) & 0xFF);
		buf[3] = (byte) ((v >>> 0) & 0xFF);
		return buf;
	}

	public static byte[] toBytes(byte[] buf, int off, int v) {
		buf[off] = (byte) ((v >>> 24) & 0xFF);
		buf[off + 1] = (byte) ((v >>> 16) & 0xFF);
		buf[off + 2] = (byte) ((v >>> 8) & 0xFF);
		buf[off + 3] = (byte) ((v >>> 0) & 0xFF);
		return buf;
	}

	public static byte[] toBytes3(int v) {
		byte buf[] = new byte[3];
		buf[0] = (byte) ((v >>> 16) & 0xFF);
		buf[1] = (byte) ((v >>> 8) & 0xFF);
		buf[2] = (byte) ((v >>> 0) & 0xFF);
		return buf;
	}

	public static byte[] toBytes3(byte[] buf, int off, int v) {
		buf[off] = (byte) ((v >>> 16) & 0xFF);
		buf[off + 1] = (byte) ((v >>> 8) & 0xFF);
		buf[off + 2] = (byte) ((v >>> 0) & 0xFF);
		return buf;
	}

	public static byte[] toBytes(long v) {
		byte buf[] = new byte[8];
		buf[0] = (byte) (v >>> 56);
		buf[1] = (byte) (v >>> 48);
		buf[2] = (byte) (v >>> 40);
		buf[3] = (byte) (v >>> 32);
		buf[4] = (byte) (v >>> 24);
		buf[5] = (byte) (v >>> 16);
		buf[6] = (byte) (v >>> 8);
		buf[7] = (byte) (v >>> 0);
		return buf;
	}

	public static byte[] toBytes(byte[] buf, int off, long v) {
		buf[off] = (byte) (v >>> 56);
		buf[off + 1] = (byte) (v >>> 48);
		buf[off + 2] = (byte) (v >>> 40);
		buf[off + 3] = (byte) (v >>> 32);
		buf[off + 4] = (byte) (v >>> 24);
		buf[off + 5] = (byte) (v >>> 16);
		buf[off + 6] = (byte) (v >>> 8);
		buf[off + 7] = (byte) (v >>> 0);
		return buf;
	}

	public static byte[] toBytes5(long v) {
		byte writeBuffer[] = new byte[5];
		writeBuffer[0] = (byte) (v >>> 32);
		writeBuffer[1] = (byte) (v >>> 24);
		writeBuffer[2] = (byte) (v >>> 16);
		writeBuffer[3] = (byte) (v >>> 8);
		writeBuffer[4] = (byte) (v >>> 0);
		return writeBuffer;
	}

	public static byte[] toBytes5(byte[] buf, int off, long v) {
		buf[off] = (byte) (v >>> 32);
		buf[off + 1] = (byte) (v >>> 24);
		buf[off + 2] = (byte) (v >>> 16);
		buf[off + 3] = (byte) (v >>> 8);
		buf[off + 4] = (byte) (v >>> 0);
		return buf;
	}
	
	public static byte[] toBytes(double v) {
		return toBytes(Double.doubleToLongBits(v));
	}
	
	public void writeDouble(double v) throws IOException {
		
		//this.dot.write(toBytes(v));
		this.dot.writeDouble(v);
		
	}
	
	public  void writeText(String s) throws IOException {
		if (s == null) {
			dot.write((byte)0);
		} else {
			writeBlob(s.getBytes("UTF8") );
		}		
	}
	
	
	
	public void writeDecimalArray(int[] v) throws IOException {
		if (v == null) {
			writeDecimal(0);
		} else {
			writeDecimal(v.length);
			for (int i = 0; i < v.length; i++) {
				writeDecimal(v[i]);
			}
		}
		
	}
	public void writeDecimalArray(long[] v) throws IOException {
		if (v == null) {
			writeDecimal(0);
		} else {
			writeDecimal(v.length);
			for (int i = 0; i < v.length; i++) {
				writeDecimal(v[i]);
			}
		}
		
	}
	public void writeFloat(float v) throws IOException {
		
		//this.dot.write(toBytes(v));
		this.dot.writeFloat(v);

	}
	
	
	public  void writeByte(byte v) throws IOException {
		dot.writeByte(v);
	}
	
	
	public byte[] toByteArray() {
		if (this.baos != null)
			return baos.toByteArray();
		else
			return null;
	}
	
	public void write(byte[] v) throws IOException {
		dot.write(v);
	}
	
	
	public void writeBlob(byte[] value) throws IOException {
		if (value == null || value.length == 0) {
			writeByte((byte) 0);
		} else {
			int len = value.length;
			if (len <= 253) {
				writeByte((byte) len);
				write(value);
			} else if (len <= 65535) {
				byte[] buff = new byte[3];
				buff[0] = (byte) 255; // 255 means value's length is more than 253 bytes.
				write(toBytes(buff, 1, (short) len));
				write(value);
			} else {
				byte[] buff = new byte[5];
				buff[0] = (byte) 254; // 254 means value's length is more than 65535 bytes.
				write(toBytes(buff, 1, len));
				write(value);
			}
		}
		
	}
}

package kr.co.pionnet.dy.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;


public class ByteUtil {
	
	public static byte[] byteArraytoObject(byte type,Object o){
		
		byte[] oArr = byteArraytoObject(o);
		int length = oArr.length;
		byte[] destArr = new byte[length+1];
		
		destArr[0] = type;
		
		System.arraycopy(oArr, 0, destArr, 1, length);
		
		return destArr;	
	}
	
	public static byte[] byteArraytoBytes(byte type,byte[] bytes){
		
		byte[] oArr = bytes;
		int length = oArr.length;
		byte[] destArr = new byte[length+1];
		
		destArr[0] = type;
		
		System.arraycopy(oArr, 0, destArr, 1, length);
		
		return destArr;	
	}
	
	public static byte[] byteArraytoObject(Object o){
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);   
			out.writeObject(o);
			out.flush();
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		  try {  if (bos != null) {bos.close();} } catch (IOException ex) {}
		}
		return null;
	}  
	
	
	public static Object objectToByteArray(byte[] byteArray){
		
		ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
		ObjectInput in = null;
		try {
			in = new ObjectInputStream(bis);
			return in.readObject(); 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {if (in != null) {in.close();}} catch (IOException ex) {}
		}
		return null;
	} 
	
	public static Object objectToByteArrayRemoveType(byte[] byteArray){
		
		byte[] oArr = byteArray;
		int length = oArr.length;
		byte[] destArr = new byte[length-1];
				
		System.arraycopy(oArr, 1, destArr, 0, length-1);
		
		return objectToByteArray(destArr);

	} 
	
	
	public static byte[] byteArrayRemoveType(byte[] byteArray){
		
		byte[] oArr = byteArray;
		int length = oArr.length;

		byte[] destArr = new byte[length-1];
				
		System.arraycopy(oArr, 1, destArr, 0, length-1);
		
		return destArr;

	} 
	public static void main(String[] args) {
		
		byte[] a = new byte[4];
		a[0] = 0x00;
		a[1] = 0x01;
		a[2] = 0x02;
		a[3] = 0x03;
		
		int length = a.length;
		byte[] destArr = new byte[length+1];
		
		destArr[0] = 0x09;
		
		System.arraycopy(a, 0, destArr, 1, length);
		/*System.out.println(destArr.length);
		System.out.println(destArr[0]);
		System.out.println(destArr[1]);
		System.out.println(destArr[2]);
		System.out.println(destArr[3]);
		System.out.println(destArr[4]);*/
		
	}
}

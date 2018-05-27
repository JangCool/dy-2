package kr.co.pionnet.dy.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class EtcUtill {

	
	static public byte[] toByteArray(Object obj) {
		byte[] bytes = null;    	
		ByteArrayOutputStream bos = null;
		ObjectOutputStream  oos = null; 

		try {
				bos = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(bos);
				oos.writeObject(obj);			
				bytes = bos.toByteArray();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {				
				try {
					if(oos != null) {
						oos.flush();
						oos.close();
					}
					if(bos != null )bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}			
		
//		 System.out.println("send Bytes : "+bos.toByteArray().length);
		 
	 	 return bytes;
    }
	    
	 static public Object toObject(byte[] bytes) {
	    	Object obj = null;    	
	    	ObjectInputStream  ois = null;
	    	ByteArrayInputStream bis = null;
	    	try {
	    		bis = new ByteArrayInputStream(bytes);
					ois = new ObjectInputStream(bis);
					
					obj = ois.readObject();			
			} catch (Exception e) {
				e.printStackTrace();
			} finally {    	
				// TODO Auto-generated catch block
				try {
					if(bis != null)	bis.close();
					if(ois != null)	ois.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
	    	return obj;    		
	   }
	 
	public static int byteToInt(byte[] src) {
	        int s1 = src[0] & 0xFF;
	        int s2 = src[1] & 0xFF;
	        int s3 = src[2] & 0xFF;
	        int s4 = src[3] & 0xFF;	    
	        return ((s1 << 24) + (s2 << 16) + (s3 << 8) + (s4 << 0));
	}
	 
	public static byte[] intToByte(int value) {
	 	byte[] intToByte = new byte[4];
		intToByte[0] |= (byte)((value&0xFF000000)>>24);   
		intToByte[1] |= (byte)((value&0xFF0000)>>16);   
		intToByte[2] |= (byte)((value&0xFF00)>>8);   
		intToByte[3] |= (byte)(value&0xFF);
		return intToByte;
	}
	
	
	public static byte[] longToBytes(long l) {
        byte[] buf = new byte[8];
        buf[0] = (byte)( (l >>> 56) & 0xFF );
        buf[1] = (byte)( (l >>> 48) & 0xFF );
        buf[2] = (byte)( (l >>> 40) & 0xFF );
        buf[3] = (byte)( (l >>> 32) & 0xFF );   
        buf[4] = (byte)( (l >>> 24) & 0xFF );
        buf[5] = (byte)( (l >>> 16) & 0xFF );
        buf[6] = (byte)( (l >>>  8) & 0xFF );
        buf[7] = (byte)( (l >>>  0) & 0xFF );    
        return buf;
	}
	
	
	public static long toLong(byte[] data) {
	  return (long)((long)(0xff & data[0]) << 56 |
		   (long)(0xff & data[1]) << 48 |
		   (long)(0xff & data[2]) << 40 |
		   (long)(0xff & data[3]) << 32 |
		   (long)(0xff & data[4]) << 24 |
		   (long)(0xff & data[5]) << 16 |
		   (long)(0xff & data[6]) << 8 |
		   (long)(0xff & data[7]) << 0 );
	  }
	
	

}

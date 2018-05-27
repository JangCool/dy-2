package kr.co.pionnet.dragon.db;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.xerial.snappy.Snappy;

import kr.co.pionnet.dy.common.Config;
import kr.co.pionnet.dy.common.DataOutputTypeConverter;
import kr.co.pionnet.dy.util.EtcUtill;


public class DataFileRW {

	
	private RandomAccessFile raf;
	private static String path;
	FileChannel outputFileChannel;
	
	private final int FIXED_DATASIZE_LENGTH = 4;
	private static DataFileRW instance = null;
	
	private static int offset = 0;
	
	private  boolean blnCompression = Config.getBoolean("data.object.compression", false);
	
	public static DataFileRW getInstance(String filePath) {
		setConfig(filePath);
		instance = new DataFileRW();
		return instance;
		
	}
	
	
	
	public DataFileRW() {	
		File file = new File(path.concat(".df"));
		try {
			raf = new RandomAccessFile(file, "rw");
		} catch (FileNotFoundException e) {		
			e.printStackTrace();
		}		
	}
	
	public static void setConfig(String filePath) {
		path = filePath;				
	}
	
	public void close() throws IOException {
		if(instance != null ) {
			instance = null;
		}
		if(raf != null ){
			raf.close();
		}
	}
	
		
	public byte[] write(byte[]  data) throws Exception {
		synchronized (this) {
			int filePos = -1;
			outputFileChannel = raf.getChannel();
			
			byte[] bytes = data;
			int dataSize = bytes.length;
			//데이타 사이즈대한 자리(4byte) + dataSize(4byte)
			int writeDataSize = FIXED_DATASIZE_LENGTH+dataSize;
			ByteBuffer inBuf = ByteBuffer.allocateDirect(writeDataSize);			
			ByteArrayOutputStream baos = null;
			
			try {
				inBuf.clear();
				inBuf.put(DataOutputTypeConverter.toBytes(dataSize));  //4byte dataSize 저장
				inBuf.put(bytes);  // 데이타 저장
				inBuf.flip();   
				filePos = (int) raf.length();
				raf.seek (filePos);     
				
				outputFileChannel.write(inBuf);		
								
				
				DataObjInfo dataInfo = new DataObjInfo(dataSize, filePos);
				baos = new ByteArrayOutputStream();				
				dataInfo.writeStream(baos);
				
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				if(inBuf != null) {
					destroyDirectByteBuffer(inBuf);
				}
			}
			

			return baos.toByteArray();
			
		}
	}
	
	
	public byte[] writeProfile(byte[]  data)  {
		synchronized (this) {
			int filePos = -1;
			outputFileChannel = raf.getChannel();
			
			byte[] bytes;
			ByteBuffer inBuf = null ;
			ByteArrayOutputStream baos = null;
			
			try {
				bytes = blnCompression? Snappy.compress(data):data;
		
				int dataSize = bytes.length;
				//데이타 사이즈대한 자리(4byte) + dataSize(4byte)
				int writeDataSize = FIXED_DATASIZE_LENGTH+dataSize;
				 inBuf = ByteBuffer.allocateDirect(writeDataSize);			
				
				inBuf.clear();
				inBuf.put(DataOutputTypeConverter.toBytes(dataSize));  //4byte dataSize 저장
				inBuf.put(bytes);  // 데이타 저장
				inBuf.flip();   
				filePos = (int) raf.length();
				raf.seek (filePos);     
				
				outputFileChannel.write(inBuf);		
								
				
				DataObjInfo dataInfo = new DataObjInfo(dataSize, filePos);
				baos = new ByteArrayOutputStream();				
				dataInfo.writeStream(baos);
				
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				if(inBuf != null) {
					destroyDirectByteBuffer(inBuf);
				}
			}
			
			return baos != null ?  baos.toByteArray(): new byte[0];
			
		}
	}
	public byte[] getValue(long pos,  int objectSize) throws IOException {
		byte[] byteBuf = new byte[objectSize];
		
		synchronized (this) {
			this.raf.seek(pos);
			raf.read (byteBuf);
			
			return byteBuf;
		}
	}

	public Object getDataValue(long pos, int valueSize) throws IOException {				
		byte[] bytes = getValue(pos, valueSize);
		return EtcUtill.toObject(bytes);
	}

	
	
	 public static void destroyDirectByteBuffer(ByteBuffer toBeDestroyed)    {
		    
		 
		    Method cleanerMethod;
			try {
				cleanerMethod = toBeDestroyed.getClass().getMethod("cleaner");
				cleanerMethod.setAccessible(true);
				Object cleaner = cleanerMethod.invoke(toBeDestroyed);
				Method cleanMethod = cleaner.getClass().getMethod("clean");
				cleanMethod.setAccessible(true);
				cleanMethod.invoke(cleaner);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		  
	}

}




package kr.co.pionnet.dragon.db;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import kr.co.pionnet.dy.util.EtcUtill;

public  class DataInfoFileRW {

	private RandomAccessFile raf;
	
	private static String path;
	private static String txMod = "rw";
	
	
	

	private static DataInfoFileRW instance = null;
	
	
	public static DataInfoFileRW getInstance(String filePath, String rw){
			instance = new DataInfoFileRW(filePath, rw);
		return instance;
	}
	

	
	private DataInfoFileRW(String path, String rw)  {		
		
		try {
	
			File file = new File(path.concat(".idx"));
			raf = new RandomAccessFile(file, "rw");
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void close()  {
		try {			
			
			if(instance != null) {
				instance = null;
			}
			if(raf != null ){
				raf.close();
			}
			
			
		}	catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public long write( byte[] bytes) throws Exception {
		synchronized (this) {
			long filePos = -1;			
			FileChannel outputFileChannel = raf.getChannel();			
			int size = bytes.length;
			ByteBuffer inBuf = ByteBuffer.allocateDirect(size);
			try {
				inBuf.clear();
				inBuf.put(bytes);
				inBuf.flip();
				filePos = raf.length();
				raf.seek (filePos);
				outputFileChannel.write(inBuf);	
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				if(inBuf != null)  {
					destroyDirectByteBuffer(inBuf);
				}
			}
			return filePos;	
		}
		
	}
	
	
	
	

	
	//
	public Object getIndexValue(long pos,  int keySize) throws IOException {
		byte[] byteBuf = new byte[keySize];		
		synchronized (this) {
			this.raf.seek(pos);
			raf.read (byteBuf);			
			return EtcUtill.toObject(byteBuf);
		}
	}
	
	
	
	 public static void destroyDirectByteBuffer(ByteBuffer toBeDestroyed) throws Exception{
		    Method cleanerMethod = toBeDestroyed.getClass().getMethod("cleaner");
		    cleanerMethod.setAccessible(true);
		    Object cleaner = cleanerMethod.invoke(toBeDestroyed);
		    Method cleanMethod = cleaner.getClass().getMethod("clean");
		    cleanMethod.setAccessible(true);
		    cleanMethod.invoke(cleaner);
	}

	

	
	
	
	

}

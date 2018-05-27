package kr.co.pionnet.dragon.db;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

import kr.co.pionnet.dy.util.EtcUtill;

public class IndexFileRW {

	private RandomAccessFile raf;
	
	private static IndexFileRW instance = null;
	private  KeyMapMng<?, ?> indexMapMng = null;
	
	public static IndexFileRW getInstance(String filePath, String rw){
		if(instance == null)  {			
			instance = new IndexFileRW(filePath, rw);			
		}		
		return instance;
	}
	
	public void hashOpen() {
		try {
			indexMapMng.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void hashPut(long tx_id, int value) {		
		indexMapMng.put(tx_id, value);
	}
	

	
	private IndexFileRW(String path, String rw)  {		
		
		try {
			indexMapMng = new KeyMapMng(path, rw, 3000, 2048);
			File file = new File(path.concat(".dpf"));
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
			if(raf != null ) {
				raf.close();
			}
			
			if(indexMapMng != null) { 
				indexMapMng.close();
			
			}
		}	catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public Object getIndexValue(long pos,  int keySize) throws IOException {
		byte[] byteBuf = new byte[keySize];		
		synchronized (this) {
			this.raf.seek(pos);
			raf.read (byteBuf);			
			return EtcUtill.toObject(byteBuf);
		}
	}
	
	 public static void destroyDirectByteBuffer(ByteBuffer toBeDestroyed)
		      throws Exception{

		    
		    Method cleanerMethod = toBeDestroyed.getClass().getMethod("cleaner");
		    cleanerMethod.setAccessible(true);
		    Object cleaner = cleanerMethod.invoke(toBeDestroyed);
		    Method cleanMethod = cleaner.getClass().getMethod("clean");
		    cleanMethod.setAccessible(true);
		    cleanMethod.invoke(cleaner);
	}

	


}




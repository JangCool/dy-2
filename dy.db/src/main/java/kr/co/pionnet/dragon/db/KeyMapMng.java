package kr.co.pionnet.dragon.db;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;

import kr.co.pionnet.dy.common.DataOutputTypeConverter;

public class KeyMapMng<K, V> {

	
	String path ;
	boolean existsFile = false;
	//HashMap<Long, String> tmpMap = null;
	
	
	
	private TreeMultimap<Long, Integer> multimap  = null;;
	 //ConcurrentHashMap table = null;
	
	Path path1 = null;
	
	int capacity = 10000000;
	int interval = 5000;
	
	boolean flush = false;
	
	String rw ;
	
    private  KeyMapMng instance = null;
	public   KeyMapMng getInstance(String path, String rw, int interval, int capacity)  {
		if(instance == null )
			try {
				instance = new KeyMapMng(path, rw, interval, capacity);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return instance;
	}
	
	public KeyMapMng(String path, String rw, int interval, int capacity) throws Exception {
		this.rw = rw;
		this.path = path.concat(".key");
		File file1 = new File(this.path);
		
		
		file1.createNewFile();
		
		
		if(interval > 0) this.interval = interval;
		if(capacity > 0) 	this.capacity = capacity;
		//open();
	}

	public long interval() {
		return this.interval;
	}
	

	
	public boolean isFlush() {
		return flush;
	}
	
	public void open() throws Exception {
		File file = new File(path);		
		this.existsFile = file.exists();
		//if(multimap != null) multimap = null;
		 
		multimap = TreeMultimap.create(Ordering.natural(), new Comparator<Integer>() { 
		   @Override 
            public int compare(Integer o1, Integer o2) { 
                return o1 > o2 ? 1:-1; 
            } 
        }); 
     
        
		//hashMap = new HashMap<Long, ArrayList<String>>(capacity);
		if(!"r".equals(rw.toLowerCase())) {
			HashTableToFile.getInstance().put(this);
		}
	}
	
	
	public synchronized void put(long key, int value) {
		multimap.put(key, value);		
		flush = true;			
	}
	
	private synchronized  void write(byte[] bytes) throws IOException, URISyntaxException {
		
		if(bytes != null && bytes.length > 0) {		
			 path1 = Paths.get(this.path);
			 SeekableByteChannel sbc = Files.newByteChannel(path1, StandardOpenOption.APPEND);
			 ByteBuffer bfSrc = ByteBuffer.wrap(bytes);
			 sbc.write(bfSrc);
			 sbc.close();			 
			// writeTxt(bytes);
		}
	}

	
/*	
	private synchronized  void writeTxt(byte[] bytes) throws IOException, URISyntaxException {
		
		if(bytes != null && bytes.length > 0) {	
			 path1 = Paths.get(this.path+".txt");
			 SeekableByteChannel sbc = Files.newByteChannel(path1, StandardOpenOption.APPEND);			 
			 ByteBuffer bfSrc = ByteBuffer.wrap(bytes);			 
			 String msg = String.valueOf(bfSrc.getLong())+":"+String.valueOf(bfSrc.getInt())+",";			 
			 ByteBuffer bfSrc1 = ByteBuffer.wrap(msg.getBytes());
			 sbc.write(bfSrc1);
			 sbc.close();
		}
	}*/

	
	
	public synchronized void saveTableToFile()  {		
		try {
			  Map<Long, Collection<Integer>> map = multimap.asMap();
		      for (Map.Entry<Long,  Collection<Integer>> entry : map.entrySet()) {
		         long key = entry.getKey();
		         Collection<Integer> values =  multimap.get(key);
		         
		         //동일 건이 존재 할수 있음
		         for(int value:values) {
		        	 
	        	 	byte[] bytes = new byte[12];
	        	 	byte[] keyBytes = DataOutputTypeConverter.toBytes(key);
	        	 	System.arraycopy(keyBytes, 0, bytes, 0, keyBytes.length);
	        	 	
	        	 	byte[] valueBytes = DataOutputTypeConverter.toBytes(value);
	        	 	System.arraycopy(valueBytes, 0, bytes, 8, valueBytes.length);
		        	 write(bytes);
		         }

		      }
		      
			flush = false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
		
	public void close() throws Exception {
		HashTableToFile.getInstance().remove(this);
		open();
	}
	

	
}

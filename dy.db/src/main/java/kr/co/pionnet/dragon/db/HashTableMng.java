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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class HashTableMng<K, V> {

	
	String path ;
	boolean existsFile = false;
	//HashMap<Long, String> tmpMap = null;
	
	
	
	private Multimap<Long,String> multimap  = null;;
	 //ConcurrentHashMap table = null;
	
	Path path1 = null;
	
	int capacity = 10000000;
	int interval = 4000;
	File file = null;
	boolean flush = false;
	
	String rw ;
	
    private  HashTableMng instance = null;
	public   HashTableMng getInstance(String path, String rw, int interval, int capacity)  {
		if(instance == null )
			try {
				instance = new HashTableMng(path, rw, interval, capacity);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return instance;
	}
	
	public HashTableMng(String path, String rw, int interval, int capacity) throws Exception {
		this.rw = rw;
		this.path = path.concat(".hf");
		File file1 = new File(this.path);
		file1.createNewFile();
		
		if(interval > 0) this.interval = interval;
		if(capacity > 0) 	this.capacity = capacity;
		open();
	}

	public long interval() {
		return this.interval;
	}
	

	
	public boolean isFlush() {
		return flush;
	}
	
	public void open() throws Exception {
		file = new File(path);		
		this.existsFile = file.exists();
		if(multimap != null) multimap = null;
		 
		multimap = ArrayListMultimap.create();
		//hashMap = new HashMap<Long, ArrayList<String>>(capacity);
		if(!"r".equals(rw.toLowerCase())) {
			HashTableToFile.getInstance().put(this);
		}
	}
	
	
	public synchronized void put(long key, String value) {
		multimap.put(key, value);
		flush = true;
	}
	
	private synchronized  void write(String msg) throws IOException, URISyntaxException {
		
		String pattern = "-?\\d+\\:\"\\d+\\^\\d+\",";		
		Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(msg);
		
//		System.out.println("msg ====>: "+msg);
		if(matcher.matches()) {
		
		 path1 = Paths.get(this.path);
		 SeekableByteChannel sbc = Files.newByteChannel(path1, StandardOpenOption.APPEND);
		 ByteBuffer bfSrc = ByteBuffer.wrap(msg.getBytes());
		 sbc.write(bfSrc);
		 sbc.close();
		} else {
//			System.out.println("no Pattern ====>: "+msg);
		}
	}
	
	
	
	public synchronized void saveTableToFile()  {		
		try {			
			
			//Multimap<Long, String> tmpMultiMap = ArrayListMultimap.create(multimap);
			//tmpMultiMap.putAll(tmpMultiMap);
			
/*			Map<Long, String>  map = new HashMap();
			map.putAll(tmpMap);
			for( Map.Entry<Long, String> elem : map.entrySet()){	
	            write(Long.toString(elem.getKey()).concat(":").concat("\"").concat(elem.getValue()).concat("\"").concat(","));	            
	        }

			*/
			  Map<Long, Collection<String>> map = multimap.asMap();
		      for (Map.Entry<Long,  Collection<String>> entry : map.entrySet()) {
		         long key = entry.getKey();
		         Collection<String> value =  multimap.get(key);
//		         System.out.println(key + ":" + value);
		         //write(Long.toString(elem.getKey()).concat(":").concat("\"").concat(elem.getValue()).concat("\"").concat(","));	           
		         //System.out.println("hashMap size ==> "+hashMap.size());
		      }
		      
		      
			
			/*for( Map.Entry<Long, String> elem : tmpMultiMap.entries()){	
	            write(Long.toString(elem.getKey()).concat(":").concat("\"").concat(elem.getValue()).concat("\"").concat(","));	            
	        }
			*/
			
			
			
			
			   
			   

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

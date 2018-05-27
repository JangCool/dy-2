package kr.co.pionnet.dragon.db;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.xerial.snappy.Snappy;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;

import kr.co.pionnet.dy.common.Config;
import kr.co.pionnet.dy.type.DataType;
import kr.co.pionnet.dy.util.CompressionUtil;
import kr.co.pionnet.dy.util.EtcUtill;
import kr.co.pionnet.dy.util.FileUtil;
import kr.co.pionnet.dy.vo.pack.ProfilePack;
import kr.co.pionnet.dy.vo.pack.TransXviewPack;

public class DBFileReader {

	
	private RandomAccessFile dataRaf;	
	 
	private byte dataType;
	
	private boolean isSingleFile = true;
	private int FIXED_DATA_INFO_LENGTH = 12;

	private String firstFilePath = "";
	private String secondFilePath = "";
	 
	private long sTime = -1;
	private long eTime = -1; 


	private  boolean blnFileCompression = Config.getBoolean("data.file.compression", false);
	private  boolean blnObjectCompression = Config.getBoolean("data.object.compression", false);
	
	String fileHome = Config.getString("data.file.path");	
	
	public DBFileReader(byte dataType) {
		this.dataType = dataType;	
	}
	
	public void close() throws IOException {			
		if(dataRaf != null ) {
			dataRaf.close();		
		}
	}
	

	//xview 데이타를 time으로  범위 검색
	public List readTracker(long tx_id, long sTime, long eTime,  int elapsed) throws IOException {		
		this.sTime = sTime;
		this.eTime = eTime;
		
		Object obj = getValue(tx_id,  sTime, eTime, elapsed);
		
		List list = new ArrayList();
		if(obj != null )  {
			list = (List<TransXviewPack>)obj;
		}
		
		return list;
	}
	

	
	public List readProfileList(long tx_id, long time) {
		List<ProfilePack> list = new ArrayList();		
		try {
			
			Object obj = getValue(tx_id, time);
			
			if(obj != null ) { 
				list = (List<ProfilePack>)obj;
			
			}
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}
	
    
	private  TreeMultimap<Long, Integer> selectMap(long sTime, long eTime) throws Exception {
		
		this.firstFilePath = getFilePath(sTime);
		this.secondFilePath = getFilePath(eTime);
		
		 TreeMultimap<Long, Integer>  map = TreeMultimap.create(Ordering.natural(), new Comparator<Integer>() { 
			   @Override 
	            public int compare(Integer o1, Integer o2) { 
	                return o1 > o2 ? 1:-1; 
	            } 
	        });
		 
		if (firstFilePath.equals(secondFilePath)) {			
			
			isSingleFile = true;
			map =   loadFile(firstFilePath);
		} else {
			
			isSingleFile = false;
			map.putAll(loadFile(firstFilePath));
			map.putAll(loadFile(secondFilePath));
		}

		return map;
	}
	
	private  String getFilePath(long time) {	
		
		if(DataType.TRACKER == dataType) {
			return  FileUtil.makeDBFilePath(fileHome, "tracker",  time, TimeUnit.MINUTES, 5 );
		}
		else return FileUtil.makeDBFilePath(fileHome, "profile",  time, TimeUnit.MINUTES, 5 );
		
	}
		
	
	private synchronized byte[] getIndexValue(long time,long pos,  int keySize) throws IOException {
		
		RandomAccessFile dataInfoRaf = null;
		ByteBuffer byteBuffer = null;
		byte[] resultBytes = null;
		try {
			
			String filePath = getFilePath(time).concat(".idx");
			if(!FileUtil.isExists(filePath)) {
				return null;
			}
			
			dataInfoRaf = new RandomAccessFile(filePath, "r");
			long length = dataInfoRaf.length();
			
			if(isSingleFile) {
				if( length - pos >= keySize) {
					byte[] bytes = new byte[keySize];
					dataInfoRaf.seek(pos);
					dataInfoRaf.read (bytes);
					byteBuffer = ByteBuffer.allocate(FIXED_DATA_INFO_LENGTH).put(bytes);				
				}
				
			} else {
				byte[] bytes1 = new byte[keySize];
				byte[] bytes2 = new byte[keySize];
				byteBuffer = ByteBuffer.allocate(FIXED_DATA_INFO_LENGTH*2);
				
				
			   if("FIRST".equals(fileTypeCheck(time))) {
					dataInfoRaf.seek(pos);
					dataInfoRaf.read (bytes1);
					byteBuffer.put(bytes1);
					
					dataInfoRaf.seek(length-FIXED_DATA_INFO_LENGTH);
					dataInfoRaf.read (bytes2);
					byteBuffer.put(bytes2);
				} else {
					
					dataInfoRaf.seek(0);
					dataInfoRaf.read (bytes1);
					byteBuffer.put(bytes1);
					
					
					dataInfoRaf.seek(pos);
					dataInfoRaf.read (bytes2);				
					byteBuffer.put(bytes2);
					
				}
				
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(dataInfoRaf != null )  {
				dataInfoRaf.close();
			}
			
		}
		
		if(byteBuffer != null ) {
			resultBytes =   byteBuffer.array();
		}
		
		return  resultBytes;
			
			
	}
	
	
	//Profile 검색시 사용
	private synchronized Object getValue(long tx_id, long eTime) throws IOException {
		return getValue(tx_id, eTime, eTime, 0);
	}
	
	
	
	
	public  Long nearestKey(TreeMultimap<Long, Integer> map, Long sTime) {
	    double minDiff = Double.MAX_VALUE;
	    Long nearest = null;
	    int idx;
	    for (long key : map.keySet()) {
	        double diff = Math.abs((double) sTime - (double) key);
	        if (diff < minDiff) {
	            nearest = key;
	            minDiff = diff;
	        }
	    }
	    
	    
	    return nearest;
	}
	

	 
	 

	private synchronized Object getValue(long tx_id, long sTime, long eTime,  int elapsed) throws IOException {

			
			Object obj = null;
			TreeMultimap<Long, Integer> map = null;
			
			Long startTime = null, endTime = null;
			
			try {
				map = selectMap(sTime, eTime);
				if(map == null )  {
					return null;
				}
				int startIndexPos=0, endIndexPos = 0 ;
					
			    if(dataType == DataType.TRACKER) {
					if(!map.containsKey(sTime)) {
						startTime = nearestKey(map, sTime);
					} else {
						startTime = sTime;
					}
					
					if(!map.containsKey(eTime)) {
						endTime = nearestKey(map, eTime);
					} else {
						endTime = eTime;
					}
					
					if(startTime != null && endTime != null) {
						startIndexPos =  map.get(startTime).first();					
						endIndexPos =  map.get(endTime).last();
					} else {
						startIndexPos = Integer.MAX_VALUE;
						endIndexPos = Integer.MAX_VALUE;
					}
					obj = getDataList(sTime, eTime, startIndexPos, endIndexPos);
					
				} else if(dataType == DataType.PROFILE) {
					
			    	startIndexPos =  map.get(tx_id).first();
			    	endIndexPos = startIndexPos;
			    	obj = getDataListProfile(sTime, eTime, startIndexPos, endIndexPos);
			    	
			    }
			    
		
			    	
			} catch (Exception e) {
				e.printStackTrace();
			}
			return obj;

	}
	

	 private  synchronized byte[]   getDataValue(long time, long fromPos, long toPos, int valueSize) throws IOException {				
		ByteBuffer byteBuf = null;
		
		RandomAccessFile dataRaf = null ;
		String compressedFile = "",  filePath = null ;
		long totalSize = 0;
		long startPos = 0;
		long endPos = 0;
		int capacity =  0;
		
		try { 
			
				filePath = getFilePath(time).concat(".df");
				
				
				
				if(blnFileCompression) {					
					if( !FileUtil.isExists(filePath))   {
						 compressedFile = filePath.concat(".compressed");
						 if(FileUtil.isExists(compressedFile)) {
							CompressionUtil.unCompression(compressedFile, false);
						 } else {
							 return new byte[0];
						 }					
					}
				} else {
					if( !FileUtil.isExists(filePath))   {
						return new byte[0];
					}
				}
				

				/*if(!FileUtil.isExists(filePath)) {
					return new byte[0];
				}*/
				dataRaf = new RandomAccessFile(filePath, "r");			
				totalSize = dataRaf.length();
				
				if(isSingleFile) {
					byte[] dataSizeByte = new byte[4];
															
						dataRaf.seek(toPos);
						dataRaf.read (dataSizeByte);
						int dataSize = ByteBuffer.wrap(dataSizeByte).getInt();
								
						//int 4byte + dataSize
						valueSize += 4 + dataSize;
						startPos = fromPos;
						capacity = valueSize;
			
				} else {				
					if("FIRST".equals(fileTypeCheck(time))) {
						startPos = fromPos;						
						endPos = toPos;
						capacity = (int) (totalSize - startPos);						
					} else {
						
						if(fromPos == toPos) {
							byte[] dataSizeByte = new byte[4];
							dataRaf.seek(fromPos);
							dataRaf.read (dataSizeByte);							
							int dataSize = ByteBuffer.wrap(dataSizeByte).getInt();
							
							//int 4byte + dataSize
							valueSize = 4 + dataSize;
							capacity = valueSize;	
						} else {
							capacity = (int) toPos;
						}

						startPos = fromPos;
						endPos = toPos;
						
						
					}
				}
				
				if(capacity > 4)   {
				
					
					
					dataRaf.seek(startPos);
					byteBuf = ByteBuffer.allocate(capacity);
					dataRaf.read (byteBuf.array());
				}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				
				if(dataRaf != null) {
					dataRaf.close();
				}
			} catch(IOException ie) {
				ie.printStackTrace();
			}
		}
		
		return byteBuf != null ? byteBuf.array():null  ;		
	}
	
	
	public synchronized  TreeMultimap<Long, Integer> loadFile(String path)  {	      
		 RandomAccessFile aFile = null ;
		
		
	      TreeMultimap<Long, Integer>  indexMap = TreeMultimap.create(Ordering.natural(), new Comparator<Integer>() { 
			   @Override 
	            public int compare(Integer o1, Integer o2) { 
	                return o1 > o2 ? 1:-1; 
	            } 
	        }); 
	  	  
			try {
								
				if(new File(path.concat(".key")).exists()) {					
					   aFile = new RandomAccessFile(path.concat(".key"),"r");
					   FileChannel inChannel = aFile.getChannel();
			            long fileSize = inChannel.size();
			            ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
			            inChannel.read(buffer);
			            buffer.flip();			            
			            long length = fileSize/12;
			            // key : index key를 말함
			            for(int i=0; i < length ; i++) {			            	
			            	long key = buffer.getLong();
			            	
			            	
			            	int pos = buffer.getInt();
			            	
			            	if(dataType == DataType.TRACKER) {
			            		if(key  >= this.sTime && key <= this.eTime  ) {
			            			indexMap.put(key, pos);
			            		}
			            	} else {
			            		indexMap.put(key, pos);
			            	}
			            }
			            
			            inChannel.close();
			            aFile.close();
				}
			} catch(Exception  e) {
				e.printStackTrace();
			} finally {
				try {
					if(aFile != null ) {
						aFile.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
					
			}
			return indexMap;
	}
	
	
	
	
	
	private synchronized Object getDataInfo(long time, long sPos, int size) throws IOException {
		if(size < FIXED_DATA_INFO_LENGTH ) {
			return null;		
		}
		Object object = null;
		List<DataObjInfo> list = new ArrayList<DataObjInfo>();
		
		byte[] dataInfoBytes =   getIndexValue(time, sPos,  size);
		
		if(dataInfoBytes != null) {
			if(isSingleFile){			
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(dataInfoBytes));
				DataObjInfo doi = (new DataObjInfo()).readStream(dis);
				object = doi;
				
			} else {
				if(dataInfoBytes.length > FIXED_DATA_INFO_LENGTH ) {
					int length = dataInfoBytes.length/FIXED_DATA_INFO_LENGTH;			
					for(int i=0; i < length; i++) {
						byte[] byteBuff = new byte[FIXED_DATA_INFO_LENGTH];
						System.arraycopy(dataInfoBytes, i*FIXED_DATA_INFO_LENGTH, byteBuff, 0, FIXED_DATA_INFO_LENGTH);
						DataInputStream dis = new DataInputStream(new ByteArrayInputStream(byteBuff));
						DataObjInfo doi = (new DataObjInfo()).readStream(dis);				   
						list.add(doi);					
					}
					object = list;
				}
				
			}	
		}
		return object;		
	}
	
	private synchronized  List getDataList(long startTime, long endTime,  long fromPos, long toPos) throws IOException {
		
		List  resultList = new ArrayList();
		List<DataObjInfo>  list = null;
		
		byte[] dataBytes;
		ByteBuffer buf = null;
		
		if(isSingleFile) {
			DataObjInfo sDataInfo; 
			DataObjInfo eDataInfo;
			
			Object obj1= getDataInfo(startTime, fromPos,  FIXED_DATA_INFO_LENGTH);
			Object obj2= getDataInfo(endTime, toPos, FIXED_DATA_INFO_LENGTH);
			
			if(obj1 != null && obj2 != null ) {
				sDataInfo = (DataObjInfo)obj1;			
				eDataInfo = (DataObjInfo)obj2;
				
				
				//read위한 data start position
				long readDataStartPos = sDataInfo.pos;
				long readDataEndPos = eDataInfo.pos;
			
				//읽어야할 Data Size
				Integer readDataSize = (int) (readDataEndPos - readDataStartPos);
				
				byte[] dataByte = getDataValue(startTime, readDataStartPos, readDataEndPos, readDataSize);
				
				if(dataByte != null)
				buf =  ByteBuffer.wrap(dataByte);
				
			}
		} else {
			
			long readDataStartPos=0, readDataEndPos = 0;
			
			Object obj1 = getDataInfo(startTime, fromPos, FIXED_DATA_INFO_LENGTH);
			if(obj1 != null )  {
				list = (List<DataObjInfo>) obj1;			
			}
			if(list != null && list.size() > 0) {
				for(int i=0 ; i <list.size(); i++ ) {
					DataObjInfo o = list.get(i);
					if(i== 0)   {
						readDataStartPos = o.pos;
					}
					if(i==1 ) {
						readDataEndPos = o.pos;					
					}
				}
				
			}
			
			
			byte[] dataByteFirst = getDataValue(startTime, readDataStartPos, readDataEndPos,  0);
			Object obj2 =  getDataInfo(endTime, toPos, FIXED_DATA_INFO_LENGTH);
			if(obj2!=null) {
				list = (List<DataObjInfo>) obj2;
			}
			
			if(list != null && list.size() > 0) {				
				for(int i=0 ; i <list.size(); i++ ) {
					DataObjInfo o = list.get(i);
					if(i== 0)  {
						readDataStartPos = o.pos;
					}
					if(i==1 ) {
						readDataEndPos = o.pos;					
					}
				}				
			}
			
			if(dataByteFirst == null) {
				dataByteFirst = new byte[0];
			}
			
			
			byte[] dataByteSecond = getDataValue(endTime, readDataStartPos, readDataEndPos,  0);			
			
			if(dataByteSecond != null) {
				
				
				dataBytes = new byte[dataByteFirst.length + dataByteSecond.length];
				
				System.arraycopy(dataByteFirst, 0, dataBytes, 0, dataByteFirst.length);
				System.arraycopy(dataByteSecond, 0, dataBytes, dataByteFirst.length, dataByteSecond.length);
			
		
				//System.out.println("eDataInfo.pos :"+eDataInfo.pos);
				buf =  ByteBuffer.wrap(dataBytes);		
			}
		}
		
		
		

		if(buf != null ) {
			while(buf.remaining()> 0) {
				
				int dataSize = buf.getInt();
			    byte[] data = new byte[dataSize];
			    buf.get(data);

			    switch (dataType) {
				case DataType.TRACKER:					
					TransXviewPack b = (TransXviewPack) new TransXviewPack().unPack(data);
					resultList.add(b);					
					break;
					
				case DataType.PROFILE:
					
					List<byte[]> profileList = (List<byte[]>) EtcUtill.toObject(data);					
					resultList = profileList;

					break;	
	
				default:
					break;
				}
			}
		}
		return resultList;
	}
	
	
	private synchronized List getDataListProfile(long startTime, long endTime,  long fromPos, long toPos) throws IOException {
		
		List  resultList = new ArrayList();
		List<DataObjInfo>  list = null;
		
		byte[] dataBytes;
		ByteBuffer buf = null;
		
		if(isSingleFile) {
			DataObjInfo sDataInfo; 
			DataObjInfo eDataInfo;
			
			Object obj1= getDataInfo(startTime, fromPos,  FIXED_DATA_INFO_LENGTH);
			Object obj2= getDataInfo(endTime, toPos, FIXED_DATA_INFO_LENGTH);
			
			if(obj1 != null && obj2 != null ) {
				sDataInfo = (DataObjInfo)obj1;			
				eDataInfo = (DataObjInfo)obj2;
				
				
				//read위한 data start position
				long readDataStartPos = sDataInfo.pos;
				long readDataEndPos = eDataInfo.pos;
			
				//읽어야할 Data Size
				Integer readDataSize = (int) (readDataEndPos - readDataStartPos);
				
				
				buf =  ByteBuffer.wrap(getDataValue(startTime, readDataStartPos, readDataEndPos, readDataSize));
				
			}
		} else {
			
			long readDataStartPos=0, readDataEndPos = 0;
			
			Object obj1 = getDataInfo(startTime, fromPos, FIXED_DATA_INFO_LENGTH);
			if(obj1 != null ) {
				list = (List<DataObjInfo>) obj1;			
			}
			if(list != null && list.size() > 0) {
				for(int i=0 ; i <list.size(); i++ ) {
					DataObjInfo o = list.get(i);
					if(i== 0)  {
						readDataStartPos = o.pos;
					}
					if(i==1 ) {
						readDataEndPos = o.pos;					
					}
				}
				
			}
			
			
			byte[] dataByteFirst = getDataValue(startTime, readDataStartPos, readDataEndPos,  0);
			Object obj2 =  getDataInfo(endTime, toPos, FIXED_DATA_INFO_LENGTH);
			if(obj2!=null) {
				list = (List<DataObjInfo>) obj2;
			}
			if(list != null && list.size() > 0) {				
				for(int i=0 ; i <list.size(); i++ ) {
					DataObjInfo o = list.get(i);
					if(i== 0) {
						readDataStartPos = o.pos;
					}
					if(i==1 ) {
						readDataEndPos = o.pos;					
					}
				}				
			}
			
			
			byte[] dataByteSecond = getDataValue(endTime, readDataStartPos, readDataEndPos,  0);			
			
			if(dataByteFirst != null && dataByteSecond != null) {
				dataBytes = new byte[dataByteFirst.length + dataByteSecond.length];
				
				System.arraycopy(dataByteFirst, 0, dataBytes, 0, dataByteFirst.length);
				System.arraycopy(dataByteSecond, 0, dataBytes, dataByteFirst.length, dataByteSecond.length);

				buf =  ByteBuffer.wrap(dataBytes);		
			}
		}
		
		
		

		if(buf != null ) {
			while(buf.remaining()> 0) {
				int dataSize = buf.getInt();

				
			    byte[] data = new byte[dataSize];
			    buf.get(data);
			    
			    if(blnObjectCompression){
			    	data = Snappy.uncompress(data);
			    }
			    DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
			    
			    
			    switch (dataType) {
				case DataType.TRACKER:
					TransXviewPack b = (TransXviewPack) new TransXviewPack().unPack(data);
					resultList.add(b);					
					break;
					
				case DataType.PROFILE:
					
					List<byte[]> profileList = (List<byte[]>) EtcUtill.toObject(data);					
					resultList = profileList;
					
					break;
	
	
				default:
					break;
				}
			}
		}
		return resultList;
	}
	
	
	private String fileTypeCheck(long time) {
		String path = getFilePath(time);
		if(this.firstFilePath.equals(path)) {
			return "FIRST";
		}
		else return "SECOND";
		
	}
	
	
	public static void main(String[] args ) {
		
		/*DBFileReader db = new DBFileReader(args[0]);
		db.readTracker(Long.parseLong(args[1]), Long.parseLong(args[2]));*/
	
	}

}


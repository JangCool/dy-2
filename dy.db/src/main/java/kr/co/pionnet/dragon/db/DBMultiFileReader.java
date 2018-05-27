package kr.co.pionnet.dragon.db;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.xerial.snappy.Snappy;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;

import kr.co.pionnet.dy.common.Config;
import kr.co.pionnet.dy.type.DataType;
import kr.co.pionnet.dy.util.CompressionUtil;
import kr.co.pionnet.dy.util.DateUtil;
import kr.co.pionnet.dy.util.EtcUtill;
import kr.co.pionnet.dy.util.FileUtil;
import kr.co.pionnet.dy.util.structure.IntKeyLinkedMap;
import kr.co.pionnet.dy.vo.AppSummaryVO;
import kr.co.pionnet.dy.vo.pack.ProfilePack;
import kr.co.pionnet.dy.vo.pack.TransXviewPack;

public class DBMultiFileReader {

	private RandomAccessFile dataRaf;

	private byte dataType;

	private int FIXED_DATA_INFO_LENGTH = 12;
	private static int FILE_GEN_PERIOD = 5;

	private boolean blnFileCompression = Config.getBoolean("data.file.compression", false);
	private boolean blnObjectCompression = Config.getBoolean("data.object.compression", false);
	

	 String fileHome = Config.getString("data.file.path");
	 
	

	public DBMultiFileReader(byte dataType) {
		this.dataType = dataType;
	}

	public void close() throws IOException {
		if (dataRaf != null) {
			dataRaf.close();
		}
	}

	public List<TransXviewPack> readTracker(long sTime, long eTime, SearchSubCondition subCondition) throws Exception {

		TreeMultimap<Long, Integer> keyMap = null;
		List<TransXviewPack> list = new ArrayList<TransXviewPack>();
		IntKeyLinkedMap<SearchCondition> map = searchFileListOfRange(sTime, eTime, subCondition);

		int[] keys = map.keyArray();
		
		for (int k : keys) {
			try {
				SearchCondition scObject = map.get(k);
				keyMap = TreeMultimap.create(Ordering.natural(), new Comparator<Integer>() {
					@Override
					public int compare(Integer o1, Integer o2) {
						return o1 > o2 ? 1 : -1;
					}
				});
	
				
				setKeyFileLoadMap(scObject, keyMap);
				
				
				if(keyMap.size() == 0) continue;
				Object obj = getValue(scObject, keyMap);
				if (obj != null) {
					list.addAll((List<TransXviewPack>) obj);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	
	
	

	public List<AppSummaryVO> readAppSummary( long  fromTime, long toTime, SearchSubCondition subCondition ) throws Exception {
		
		TreeMultimap<Long, Integer> keyMap = null;
		List<AppSummaryVO> list = new ArrayList<AppSummaryVO>();
		IntKeyLinkedMap<SearchCondition> map = searchFileListOfRange(fromTime, toTime, subCondition);
		
		int[] keys = map.keyArray();
		
		for (int k : keys) {
			try {
				SearchCondition scObject = map.get(k);
				
				keyMap = TreeMultimap.create(Ordering.natural(), new Comparator<Integer>() {
					@Override
					public int compare(Integer o1, Integer o2) {
						return o1 > o2 ? 1 : -1;
					}
				});
	
				
				setKeyFileLoadMap(scObject, keyMap);
				if(keyMap.size() == 0) continue;
				Object obj = getValue(scObject, keyMap);
				if (obj != null) {
					list.addAll((List<AppSummaryVO>) obj);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	
	}
	
public List<AppSummaryVO>  readAppSummaryList( long  fromTime, long toTime, SearchSubCondition subCondition ) throws Exception {
		
		TreeMultimap<Long, Integer> keyMap = null;
		List<AppSummaryVO>  list = new ArrayList<>();
		IntKeyLinkedMap<SearchCondition> map = searchFileListOfRange(fromTime, toTime, subCondition);
		
		
		
		int[] keys = map.keyArray();
		
		for (int k : keys) {
			try { 
				SearchCondition scObject = map.get(k);
				
				
				
				keyMap = TreeMultimap.create(Ordering.natural(), new Comparator<Integer>() {
					@Override
					public int compare(Integer o1, Integer o2) {
						return o1 > o2 ? 1 : -1;
					}
				});
	
				
				setKeyFileLoadMap(scObject, keyMap);
				
				
				if(keyMap.size() == 0) continue;
				Object obj = getValue(scObject, keyMap);
				if (obj != null) {
					
					List<AppSummaryVO> abList = (List<AppSummaryVO>) obj;
					if( abList.size() > 0) {
						list.addAll(abList);
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	
	}

	
	/*
	 * Profile �씫湲�
	 */
	public List readProfileList(long tx_id, long eTime) {
		List<ProfilePack> list = new ArrayList();
		TreeMultimap<Long, Integer> keyMap = null;
		try {

			
			String path = FileUtil.makeDBFilePath(fileHome, "profile", eTime, TimeUnit.MINUTES, FILE_GEN_PERIOD);
			
			keyMap = TreeMultimap.create(Ordering.natural(), new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					return o1 > o2 ? 1 : -1;
				}
			});
			SearchCondition searchCondition = new SearchCondition(path, tx_id, true, true, eTime, eTime);

			
			setKeyFileLoadMap(searchCondition, keyMap);

			if(keyMap.size() == 0 ) return list;
			
			Object obj = getValue(searchCondition, keyMap);

			if (obj != null) {
				list = (List<ProfilePack>) obj;
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}

	public static int getCountMinuteCount(long fromDate, long toDate, int minute) {
		long diff = toDate - fromDate;
		int mod = (int) ((diff / 1000) % 60); //遺�		
		float s = (float) (diff / 1000.0 / 60. / minute);
		int result = 0;
		if (mod >= 0) {
			result = (int) Math.ceil(s + 1.);
		}
		
		return result;
	}
	
	
	
	public static int getCountHour(long fromDate, long toDate) {
		
		int getHourDiff = 0;
    	try {
			long getDiff = toDate - fromDate;
			 getHourDiff = (int) (getDiff / ( 60 * 60 * 1000));
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	return getHourDiff+1;
	}


	public IntKeyLinkedMap<SearchCondition> searchFileListOfRange(long sTime, long eTime, SearchSubCondition subCondition) {
		IntKeyLinkedMap<SearchCondition> searchConditionMap = new IntKeyLinkedMap<SearchCondition>();
		int length = 0; 
		try {
			
			switch (dataType) {
			case DataType.TRACKER:
					length = getCountMinuteCount(sTime, eTime, 5);
					for (int i = 0; i < length; i++) {
						long startTime = new Date(DateUtil.getMinute(sTime, i * 5)).getTime();
						String startPath = FileUtil.makeDBFilePath(fileHome, "tracker", sTime, TimeUnit.MINUTES, FILE_GEN_PERIOD);
						String endPath = FileUtil.makeDBFilePath(fileHome, "tracker", eTime, TimeUnit.MINUTES, FILE_GEN_PERIOD);
						SearchCondition sc = null;
						
						if (startPath.equals(endPath)) {
			
							String path = FileUtil.makeDBFilePath(fileHome, "tracker", startTime, TimeUnit.MINUTES,	FILE_GEN_PERIOD);
							sc = new SearchCondition(path, true, true, startTime, eTime, subCondition);
							searchConditionMap.put(path.hashCode(), sc);
							break;
						} else {
							boolean isFirst = false;
							boolean isLast = false;
							if (i == 0)
								isFirst = true;
			
							if (i == length - 1) {
								startTime = eTime;
								isLast = true;
							}
							
			
							String path = FileUtil.makeDBFilePath(fileHome, "tracker", startTime, TimeUnit.MINUTES,	FILE_GEN_PERIOD);
							sc = new SearchCondition(path, isFirst, isLast, startTime, eTime, subCondition);
			
							searchConditionMap.put(path.hashCode(), sc);
							if (eTime - startTime < 0) {
								break;
							}
			
						}
					}
				
				break;
				
				
			case DataType.APPSUMMARY:
				length = getCountMinuteCount(sTime, eTime, 5);				
				
				for (int i = 0; i < length; i++) {
					long startTime = new Date(DateUtil.getMinute(sTime, i * 5)).getTime();
					String startPath = FileUtil.makeDBFilePath(fileHome, "appsummary", sTime, TimeUnit.MINUTES, 5);
					String endPath = FileUtil.makeDBFilePath(fileHome, "appsummary", eTime, TimeUnit.MINUTES, 5);
					SearchCondition sc = null;
					
					String path = FileUtil.makeDBFilePath(fileHome, "appsummary", startTime, TimeUnit.MINUTES,	5);
							
					
					if (startPath.equals(endPath)) {
						
						sc = new SearchCondition(path, true, true, startTime, eTime, subCondition);
						searchConditionMap.put(path.hashCode(), sc);
						break;
					} else {
						boolean isFirst = false;
						boolean isLast = false;
						if (i == 0)
							isFirst = true;
		
						if (i == length - 1) {
							startTime = eTime;
							isLast = true;
						}
						
						
		
						sc = new SearchCondition(path, isFirst, isLast, startTime, eTime, subCondition);

						
						searchConditionMap.put(path.hashCode(), sc);
						if (eTime - startTime < 0) {
							break;
						}
		
					}
				}
			default:
				break;
			}
			
			

		} catch(Exception e) {
			e.printStackTrace();
		}
		return searchConditionMap;

	}

	/*
	 * key�뙆�씪 濡쒕뵫 議곌굔 : �뙆�씪 1媛쒖뿉 ���빐�꽌留� 濡쒕뵫
	 */

	private void setKeyFileLoadMap(SearchCondition sc, TreeMultimap<Long, Integer> keyMap) throws Exception {

		RandomAccessFile aFile = null;
		String keyFilePath = sc.searchFilePath;	
		
		
		
		
		try {
			// String keyFilePath = getFilePath(sTime);
			
			
			if (new File(keyFilePath.concat(".key")).exists()) {
				aFile = new RandomAccessFile(keyFilePath.concat(".key"), "r");
				FileChannel inChannel = aFile.getChannel();
				long fileSize = inChannel.size();
				ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
				inChannel.read(buffer);
				buffer.flip();
				long length = fileSize / 12;
				// key : index key瑜� 留먰븿
				for (int i = 0; i < length; i++) {
					long key = buffer.getLong();
					// 泥ル쾲吏� �뙆�씪�씤寃쎌슦 fromTime 洹몃�濡� �꽆湲곌퀬 toTime��
					
					int pos = buffer.getInt();
					if (dataType == DataType.TRACKER  || dataType == DataType.APPSUMMARY) {
						if (sc.isFirst) {
							if (i == length - 1)
								sc.toTime = key;
						} else if (sc.isLast) {
							if (i == 0)
								sc.fromTime = key;

						} else {
							if (i == 0)
								sc.fromTime = key;
							if (i == length - 1)
								sc.toTime = key;
						}
						
						
						if (key >= sc.fromTime && key <= sc.toTime) {
							keyMap.put(key, pos);
						}
						
						
					} else {
						
						
						keyMap.put(key, pos);
					}
				}
				
				inChannel.close();
				aFile.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (aFile != null) {
					aFile.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private String getFilePath(long time) {

		if (DataType.TRACKER == dataType) {
			return FileUtil.makeDBFilePath(fileHome, "tracker", time, TimeUnit.MINUTES, FILE_GEN_PERIOD);
		} else if(DataType.PROFILE == dataType) {
			return FileUtil.makeDBFilePath(fileHome, "profile", time, TimeUnit.MINUTES, FILE_GEN_PERIOD);
		} else if(DataType.APPSUMMARY == dataType) {
			return FileUtil.makeDBFilePath(fileHome, "appsummary", time, TimeUnit.MINUTES, FILE_GEN_PERIOD);
		} else {
			return null;
		}

	}

	private synchronized byte[] getIndexValue(long time, long pos, int keySize) throws IOException {

		RandomAccessFile dataInfoRaf = null;
		ByteBuffer byteBuffer = null;
		byte[] resultBytes = null;
		try {

			String filePath = getFilePath(time).concat(".idx");
			
			

			if (!FileUtil.isExists(filePath)) {
				return null;
			}

			dataInfoRaf = new RandomAccessFile(filePath, "r");
			long length = dataInfoRaf.length();
			if (length - pos >= keySize) {
				byte[] bytes = new byte[keySize];
				dataInfoRaf.seek(pos);
				dataInfoRaf.read(bytes);
				byteBuffer = ByteBuffer.allocate(FIXED_DATA_INFO_LENGTH).put(bytes);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dataInfoRaf != null) {
				dataInfoRaf.close();
			}

		}

		if (byteBuffer != null) {
			resultBytes = byteBuffer.array();
		}

		return resultBytes;

	}

	public Long nearestKey(TreeMultimap<Long, Integer> map, Long sTime) {
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

	/*
	 * //Profile 寃��깋�떆 �궗�슜 private synchronized Object getValue(long tx_id, long
	 * eTime) throws IOException { return getValue(tx_id, eTime, eTime, 0);
	 * 
	 * }
	 */

	private synchronized Object getValue(SearchCondition searchCondition, TreeMultimap<Long, Integer> keyMap)
			throws IOException {

		Object obj = null;
		// TreeMultimap<Long, Integer> map = null;
		long startKey = -1, endKey = -1;

		try {

			int startIndexPos = 0, endIndexPos = 0;

			if (dataType == DataType.TRACKER || dataType == DataType.APPSUMMARY) {
				
				if (searchCondition.isFirst) {
					
					
					startKey = keyMap.containsKey(searchCondition.fromTime) ? searchCondition.fromTime	: nearestKey(keyMap, searchCondition.fromTime);
					
					endKey = searchCondition.toTime;
				} else if (searchCondition.isLast) {
					startKey = searchCondition.fromTime;

					if (keyMap != null && keyMap.size() > 0) {
						endKey = keyMap.containsKey(searchCondition.toTime) ? searchCondition.toTime
								: nearestKey(keyMap, searchCondition.toTime);
					} else {
						endKey = -1;
					}
				} else {
					
					
					startKey = searchCondition.fromTime;
					endKey = searchCondition.toTime;
				}

				try {
					startIndexPos = keyMap.get(startKey).first();
					
					
				} catch (NoSuchElementException e) {
					e.printStackTrace();
					startIndexPos = Integer.MAX_VALUE;
				}

				try {
					endIndexPos = keyMap.get(endKey).last();
					
				} catch (NoSuchElementException e) {
					e.printStackTrace();
					endIndexPos = -1;
				}


				// endIndexPos = keyMap.get(endKey).last();
				

				obj = getDataList(startKey, endKey, startIndexPos, endIndexPos, searchCondition.subCondition);

				
				
			} else if (dataType == DataType.PROFILE) {

				startIndexPos = keyMap.get(searchCondition.tx_id).first();
				endIndexPos = startIndexPos;
				obj = getDataListProfile(searchCondition.fromTime, searchCondition.toTime, startIndexPos, endIndexPos);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;

	}

	

	private synchronized byte[] getDataValue(long time, long fromPos, long toPos, int valueSize) throws IOException {
		ByteBuffer byteBuf = null;

		RandomAccessFile dataRaf = null;
		String compressedFile = "", filePath = null;
		long totalSize = 0;
		long startPos = 0;
		long endPos = 0;
		int capacity = 0;

		try {

			filePath = getFilePath(time).concat(".df");

			
			if (blnFileCompression) {
				if (!FileUtil.isExists(filePath)) {
					compressedFile = filePath.concat(".compressed");
					if (FileUtil.isExists(compressedFile)) {
						CompressionUtil.unCompression(compressedFile, false);
					} else {
						return new byte[0];
					}
				}
			} else {
				if (!FileUtil.isExists(filePath)) {
					return new byte[0];
				}
			}

			
			/*
			 * if(!FileUtil.isExists(filePath)) { return new byte[0]; }
			 */
			dataRaf = new RandomAccessFile(filePath, "r");
			totalSize = dataRaf.length();

			byte[] dataSizeByte = new byte[4];
			dataRaf.seek(toPos);
			dataRaf.read(dataSizeByte);
			int dataSize = ByteBuffer.wrap(dataSizeByte).getInt();
			// int 4byte + dataSize
			valueSize += 4 + dataSize;
			startPos = fromPos;
			capacity = valueSize;
			if (capacity > 4) {
				dataRaf.seek(startPos);
				byteBuf = ByteBuffer.allocate(capacity);
				dataRaf.read(byteBuf.array());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {

				if (dataRaf != null) {
					dataRaf.close();
				}
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}

		return byteBuf != null ? byteBuf.array() : null;
	}
	
	
	private synchronized List getDataList(long startTime, long endTime, long fromPos, long toPos, SearchSubCondition subCondition) throws IOException {

		List resultList = new ArrayList();
		List<DataObjInfo> list = null;

		byte[] dataBytes;
		ByteBuffer buf = null;

		DataObjInfo sDataInfo;
		DataObjInfo eDataInfo;

		Object obj1 = getDataInfo(startTime, fromPos, FIXED_DATA_INFO_LENGTH);
		Object obj2 = getDataInfo(endTime, toPos, FIXED_DATA_INFO_LENGTH);

		if (obj1 != null && obj2 != null) {
			sDataInfo = (DataObjInfo) obj1;
			eDataInfo = (DataObjInfo) obj2;

			// read�쐞�븳 data start position
			long readDataStartPos = sDataInfo.pos;
			long readDataEndPos = eDataInfo.pos;

			// �씫�뼱�빞�븷 Data Size
			Integer readDataSize = (int) (readDataEndPos - readDataStartPos);

			byte[] dataByte = getDataValue(startTime, readDataStartPos, readDataEndPos, readDataSize);
			
			if (dataByte != null)
				buf = ByteBuffer.wrap(dataByte);

		}

		
		
		if (buf != null) {
			while (buf.remaining() > 0) {
				int dataSize = buf.getInt();
				byte[] data = new byte[dataSize];
				buf.get(data);

				switch (dataType) {
				case DataType.TRACKER:
					TransXviewPack b = (TransXviewPack) new TransXviewPack().unPack(data);

					if(subCondition != null) {
						
						List brushList = subCondition.aid;
						int serviceHash = subCondition.serviceHash;
						long sElapsed = subCondition.sElapsed;
						long eElapsed = subCondition.eElapsed;
						long sSqlTime = subCondition.sSqlTime;
						long eSqlTime = subCondition.eSqlTime;
						long sCpuTime = subCondition.sCpuTime;
						long eCpuTime = subCondition.eCpuTime;						
						long minElasped = subCondition.minElapsed;
						boolean errCheck = subCondition.errCheck;
						String c_ip = subCondition.c_ip;
						int err_cls = subCondition.err_cls;
						List filterValues = subCondition.filterValues;

						if(subCondition.isExcludeFilter) {
							if( filterValues != null && filterValues.size() > 0 && filterValues.contains(b.serv_h)   ) {
								continue;
							}	
						}else {
							if( filterValues != null && filterValues.size() > 0 && !filterValues.contains(b.serv_h)   ) {
								continue;
							}							
						}
						
						if(serviceHash != -1 && !(serviceHash == b.serv_h)) {							
							continue;
						}						
						
						if( (subCondition.startTime > -1 && subCondition.endTime > -1 ) && !(b.eTime < subCondition.endTime && b.eTime >= subCondition.startTime)) {
							continue;
						}
										
						
						if(!( minElasped  <= b.elapsed )) {
							continue;
						}
						
						
						// �쓳�떟�떆媛� 泥댄겕 �룷�솮�릺吏� �븡��寃쎌슦 
						if((sElapsed > -1 && eElapsed > -1 ) && !(b.elapsed <= sElapsed && b.elapsed >= eElapsed)) {
							continue;
						}
						
						if((sSqlTime > -1 && eSqlTime > -1 ) && !(b.sql_t <= sSqlTime && b.sql_t >= eSqlTime)) {
							continue;
						}
						
						if((sCpuTime > -1 && eCpuTime > -1 ) && !(b.cpu_t <= sCpuTime && b.cpu_t >= eCpuTime)) {
							continue;
						}
						
						if(c_ip != null && !"".equals(c_ip) && b.r_ip != null && !(b.r_ip.matches(c_ip))) {
							continue;
						}
						
						//error type 이 try_catch 인 경우에도 보여준다.
						if( (errCheck && b.e_type == 0 && !(Math.abs(b.e) > 0)) ) {
							continue;
						}
						
						if( err_cls != -1 && !(err_cls == b.e_cls)) {
							continue;
						}
						
						
						
						
						if( brushList != null &&  !brushList.contains(b.aid)   ) {
							continue;
						}
						
						resultList.add(b);
					}
					

					break;

				case DataType.PROFILE:
					List<byte[]> profileList = (List<byte[]>) EtcUtill.toObject(data);

					resultList = profileList;
					break;
					
				case DataType.APPSUMMARY:
					
					
					//	AppSummaryVO  AppSummaryVO = (AppSummaryVO) EtcUtill.toObject(data);					
						
						List<Map<Integer, AppSummaryVO>>  lists = (List<Map<Integer, AppSummaryVO>>) EtcUtill.toObject(data);
						
						
						
						
						List<AppSummaryVO> appList = lists.stream().map(m -> m.values()).flatMap(l -> l.stream()).collect(Collectors.toList());
						
						
						
						
						
						
						List<AppSummaryVO>  appResultList = new ArrayList<> ();
						if(subCondition != null) {		
							for( AppSummaryVO ab : appList) {
											
										List brushList = subCondition.aid;
										int serviceHash = subCondition.serviceHash;
										
										
										
										
										if(serviceHash != -1 && !(serviceHash == ab.app_hash)) {							
										
										//if(serviceHash != -1 && !(m.containsKey(serviceHash))) {
											continue;
										}						
										
										if( (brushList != null && !brushList.contains(ab.aid) )  ) {
											continue;
										}						
										
										String logDt = "";
										
										long logTime = 0; 
										DateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
										try {
											 logDt = ab.log_dt+ab.hh+ab.mm;
											 
											 logTime = simpleDateFormat.parse(logDt).getTime();
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}	
										
										if( (subCondition.startTime > -1 && subCondition.endTime > -1 ) && !(logTime < subCondition.endTime && logTime >= subCondition.startTime)) {
											continue;
										}
							
										appResultList.add(ab);
								}
								
								
								
							
							resultList = appResultList;
						}
					break;
				default:
					break;
				}
			}
		}
		
		return resultList;
	}
	
	

	private synchronized List getDataList(long startTime, long endTime, long fromPos, long toPos) throws IOException {

		List resultList = new ArrayList();
		List<DataObjInfo> list = null;

		byte[] dataBytes;
		ByteBuffer buf = null;

		DataObjInfo sDataInfo;
		DataObjInfo eDataInfo;

		Object obj1 = getDataInfo(startTime, fromPos, FIXED_DATA_INFO_LENGTH);
		Object obj2 = getDataInfo(endTime, toPos, FIXED_DATA_INFO_LENGTH);

		if (obj1 != null && obj2 != null) {
			sDataInfo = (DataObjInfo) obj1;
			eDataInfo = (DataObjInfo) obj2;

			// read�쐞�븳 data start position
			long readDataStartPos = sDataInfo.pos;
			long readDataEndPos = eDataInfo.pos;

			// �씫�뼱�빞�븷 Data Size
			Integer readDataSize = (int) (readDataEndPos - readDataStartPos);

			byte[] dataByte = getDataValue(startTime, readDataStartPos, readDataEndPos, readDataSize);

			if (dataByte != null)
				buf = ByteBuffer.wrap(dataByte);

		}

		if (buf != null) {
			while (buf.remaining() > 0) {
				int dataSize = buf.getInt();
				byte[] data = new byte[dataSize];
				buf.get(data);
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

	private synchronized Object getDataInfo(long time, long sPos, int size) throws IOException {
		if (size < FIXED_DATA_INFO_LENGTH) {
			return null;
		}
		Object object = null;
		List<DataObjInfo> list = new ArrayList<DataObjInfo>();

		
		
		byte[] dataInfoBytes = getIndexValue(time, sPos, size);
		

		if (dataInfoBytes != null) {
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(dataInfoBytes));
			DataObjInfo doi = (new DataObjInfo()).readStream(dis);
			object = doi;
		}
		return object;
	}

	private synchronized List getDataListProfile(long startTime, long endTime, long fromPos, long toPos)
			throws IOException {
		List resultList = new ArrayList();
		List<DataObjInfo> list = null;

		byte[] dataBytes;
		ByteBuffer buf = null;

		DataObjInfo sDataInfo;
		DataObjInfo eDataInfo;

		Object obj1 = getDataInfo(startTime, fromPos, FIXED_DATA_INFO_LENGTH);
		Object obj2 = getDataInfo(endTime, toPos, FIXED_DATA_INFO_LENGTH);

		if (obj1 != null && obj2 != null) {
			sDataInfo = (DataObjInfo) obj1;
			eDataInfo = (DataObjInfo) obj2;
			// read�쐞�븳 data start position
			long readDataStartPos = sDataInfo.pos;
			long readDataEndPos = eDataInfo.pos;

			// �씫�뼱�빞�븷 Data Size
			Integer readDataSize = (int) (readDataEndPos - readDataStartPos);
			buf = ByteBuffer.wrap(getDataValue(startTime, readDataStartPos, readDataEndPos, readDataSize));
			

		}

		if (buf != null) {
			while (buf.remaining() > 0) {
				int dataSize = buf.getInt();

				byte[] data = new byte[dataSize];
				buf.get(data);

				if (blnObjectCompression) {
					data = Snappy.uncompress(data);
				}
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
			
				// byte[] profileData = new byte[dataSize];
				List<byte[]> profileList = (List<byte[]>) EtcUtill.toObject(data);
				resultList = profileList;
			
			}
		}
		return resultList;
	}

	/*
	 * private String fileTypeCheck(long time) { String path =
	 * getFilePath(time); if(this.firstFilePath.equals(path)) { return "FIRST";
	 * } else return "SECOND";
	 * 
	 * }
	 */

	
	public static void searchCallTree(long txid, long eTime) throws Exception{	
			
		List profileList = new ArrayList();
		DBMultiFileReader dbFileReader = new DBMultiFileReader(DataType.PROFILE);
		
		List<byte[]> prolist = dbFileReader.readProfileList(txid, eTime);
		

		if(prolist == null){
			prolist = new ArrayList();
		}
		
		
		ProfilePack prev = null;
		int profileLength = prolist.size();
		
		for (int i = 0; i < profileLength ; i++) {
			
			ProfilePack b = (ProfilePack) new ProfilePack().unPack(prolist.get(i));
					
			Map p  = new HashMap();

			p.put("TXID"			, txid);
			p.put("LEVEL"			, b.level);
			p.put("SQL_TYPE"		, b.sqlType);
			p.put("STIME"			, b.eTime-b.elapsed);
			p.put("ETIME"			, b.eTime);
			p.put("ELAPSED"			, b.elapsed);
			p.put("EXTERNALAPI"		, b.externalApi);
			p.put("STEP"		, b.step);
			
			long value = 0;
			//GAP �떆媛� 泥섎━.
			if(b.sqlType == 0  && b.eTime > 0){
				
				if(i > 0){					
					if(prev != null){
						value = (b.eTime-b.elapsed) - (prev.eTime-prev.elapsed); // current StartTime  - prev StartTime
					}
				}
				
				prev = b;
			}
			p.put("P_GAP"		, value);
			int sqlType = (int)b.sqlType ;
	
			profileList.add(p);			

		}
		
		dbFileReader.close();

	}

	
	
	
}

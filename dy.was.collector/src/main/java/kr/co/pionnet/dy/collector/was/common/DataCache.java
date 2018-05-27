package kr.co.pionnet.dy.collector.was.common;

import kr.co.pionnet.dy.type.DataType;
import kr.co.pionnet.dy.util.structure.IntLinkedSet;

public class DataCache  {
	
	private static  IntLinkedSet serviceMap = null;
	//private static  IntLinkedSet qeuryMap = null; 
	//private static  IntLinkedSet userAgentMap = null; 
	//private static  IntLinkedSet refererMap = null; 
	private static  IntLinkedSet sqlText = null;
//	private static  IntLinkedSet sqlParam = null;
	private static  IntLinkedSet profileHash = null;	
	private static  IntLinkedSet errorHash = null;	
	private static  IntLinkedSet jdbcInfoHash = null;

	
	
	static {		
		if(serviceMap == null) serviceMap = new IntLinkedSet().setMax(2000);
		//if(qeuryMap == null) qeuryMap = new IntLinkedSet().setMax(2000); 
		//if(userAgentMap == null) userAgentMap = new IntLinkedSet().setMax(1000); 
		//if(refererMap == null) refererMap = new IntLinkedSet().setMax(1000); 
		if(sqlText == null) sqlText = new IntLinkedSet().setMax(1000);
//		if(sqlParam == null) sqlParam = new IntLinkedSet().setMax(1000);
		if(profileHash == null) profileHash = new IntLinkedSet().setMax(100);
		if(errorHash == null) errorHash = new IntLinkedSet().setMax(500);
		if(jdbcInfoHash == null) jdbcInfoHash = new IntLinkedSet().setMax(50);
		
	}

	
	public static int getCount(byte dataType) {
		
		int size = 0;
		if(DataType.SQLTEXT == dataType) {
			
			size =  sqlText.getArray().length;
		}
		
		return size;
		
	}
	
	public static void push(byte dataType, int hash) {
		switch (dataType) {
		case DataType.SERVICE:
			serviceMap.put(hash);
			break;
		case DataType.QUERYSTRING:
		//	qeuryMap.put(hash);
			break;
		case DataType.USERAGENT:
			//userAgentMap.put(hash);
			break;
		case DataType.REFERER:
		//	refererMap.put(hash);
			break;
		case DataType.SQLTEXT:
			sqlText.put(hash);
			break;
//		case DataType.SQLPARAM:
//			sqlParam.put(hash);
//			break;
		case DataType.PROFILEHASH:
			profileHash.put(hash);
			break;			
		case DataType.ERROR:
			errorHash.put(hash);
			break;		
		case DataType.JDBCINFO:
			jdbcInfoHash.put(hash);
			break;		
		}
	}
	
	public static void remove(byte dataType, int hash) {
		switch (dataType) {
		case DataType.SERVICE:			
			//System.out.println("serviceMap ==>"+serviceMap.size());
			serviceMap.remove(hash);
			break;
		case DataType.QUERYSTRING:
			//qeuryMap.remove(hash);
			break;
		case DataType.USERAGENT:
			//userAgentMap.remove(hash);
			break;
		case DataType.REFERER:
			//refererMap.remove(hash);
			break;
		case DataType.SQLTEXT:
			sqlText.remove(hash);
			break;
//		case DataType.SQLPARAM:
//			sqlParam.put(hash);
//			break;
		case DataType.PROFILEHASH:
			profileHash.remove(hash);
			break;		
		case DataType.ERROR:
			errorHash.remove(hash);
			break;			
		case DataType.JDBCINFO:
			jdbcInfoHash.remove(hash);
			break;		
		}
	}
	
	public static  boolean exists(byte dataType, int hash) {
		boolean result = false;		
		switch (dataType) {			
		case DataType.SERVICE:			
			result = serviceMap.contains(hash);			
			break;
		case DataType.QUERYSTRING:
			//result =  qeuryMap.contains(hash);
			break;
		case DataType.USERAGENT:
			//result =  userAgentMap.contains(hash);			
			break;
		case DataType.REFERER:			
			//result =  refererMap.contains(hash);
			break;
		case DataType.SQLTEXT:			
			result =  sqlText.contains(hash);
			break;
//		case DataType.SQLPARAM:			
//			result =  sqlParam.contains(hash);
//			break;
		case DataType.PROFILEHASH:			
			result =  profileHash.contains(hash);
			break;
		case DataType.ERROR:			
			result =  errorHash.contains(hash);
			break;
		case DataType.JDBCINFO:			
			result =  jdbcInfoHash.contains(hash);
			break;			
		}
		return result;
	}
}

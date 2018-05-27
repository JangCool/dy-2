package  kr.co.pionnet.dy.collector.was.common;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import kr.co.pionnet.dy.common.BaseConst;

public interface Const extends BaseConst{

	final public static AtomicBoolean running   = new AtomicBoolean( true );
	final public static AtomicInteger activeThredCnt = new AtomicInteger();
	
	String WORK_TYPE_DATA_CENTER = "01";
	String WORK_TYPE_XVIEW = "10";
	String WORK_TYPE_PERFORMANCE = "20";
	String WORK_TYPE_CONFIG = "30";
	
	//캐시데이타 삭제
	final static public String CACHE_JOB_DEL_01 = "D01";
	//캐시데이타 저장
	final static public String CACHE_JOB_PUT_01 = "P01";
	//캐시데이타 조회
	final static public String CACHE_JOB_GET_01 = "G01";
	
	
	//
	final static public String SYSTEM_TRACE_PATH = "S01";
	
	//캐시서버 종류선택
	final static public String CahceType_01 = "CACHE_01";
	final static public String CahceType_02 = "CACHE_02";
	
	//phantomjs 
	final String 	X_VIEW_SESSIONID  = "admin";
	final int 		X_VIEW_WIDTH  = 511;
	final int		X_VIEW_HEIGHT  = 283;
	
	final String ORACLE_JAVA = "ORACLE";
	final String IBM_JAVA = "IBM";
	
	/*
	 BC_FUNC 테이블 FUNC_SEQ 
	 */
	short BF_WAS_TR_LOG = 114;
	
}

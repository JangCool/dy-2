package kr.co.pionnet.dy.common;

public interface BaseConst {

	
	String BOOLEAN_TRUE="Y";
	String BOOLEAN_FALSE="N";
	
	String UDP = "UDP";
	String TCP = "TCP";
	String WS  = "WS"; //웹소켓 프로토콜 ws://
	
	int UDP_DEFAULT_PACKET_SIZE = 60000;
	
    byte PROFILE_TYPE_OVERFLOW_METHOD 		= -1;
    byte PROFILE_TYPE_METHOD 				= 1;
    byte PROFILE_TYPE_SQL    				= 2;
    byte PROFILE_TYPE_API    				= 3;
    byte PROFILE_TYPE_SOCKET    			= 4;
	
    byte SERVER_STATUS_OFF    				= 0X00;
    byte SERVER_STATUS_ON	    			= 0X01;
    byte SERVER_STATUS_AGENT_STOP			= 0X02;

	String SERVER_TYPE_COLLECTOR = "CO"; //콜렉터 서버
	String SERVER_TYPE_WAS	 	 = "WA"; //WAS 서버
	String SERVER_TYPE_DB		 = "DB"; //DB 서버
	
	
    /** 환경설정 값에서 Null값 대신에 "" 을 가져오기 key */
    String KEY_CONVERT_CONFIG_NULLSTRING_IGNORE = "convert.config.NullString.ignore";

    /** HttpServletRequest.getParameter에서 NumberFormatException이 발생하면 0값 가져오기 key */
    String KEY_CONVERT_REQUET_NUMBERFORMATEXCEPTION_IGNORE = "convert.request.NumberFormatException.ignore";

    /** Util package에서 NumberFormatException이 발생하면 0값 가져오기 key */
    String KEY_CONVERT_UTIL_NUMBERFORMATEXCEPTION_IGNORE = "convert.util.NumberFormatException.ignore";
    

	/*
	 Noti Info - 알림
	*/
	int DB_LOCK = 110;
	int DB_CONN_FAIL = 120;
	int DB_CPU = 130;
	int DB_TMPTBL = 140;
	int DB_TMPTBL_R = 141;
	int DB_LSN_EXP = 150;
	int DB_FILE_USED = 160;
	int DB_TBLSPACE_USED = 161;
	int DB_ALERTLOG_ERR = 170;
	
	int WAS_LSN_EXP = 1;
	int WAS_SHUTDOWN = 2;
	int CO_SHUTDOWN = 3;
	int NOTI_SYS_CPU = 30;
	int NOTI_PRC_CPU = 40;
	int NOTI_SYS_MEM = 50;
	int NOTI_JVM_H_MEM = 60;
	int FILE_SYSTEM_DISK_USAGE = 70;
	
	int APP_ERR = 210;
   
}

package kr.co.pionnet.dy.type;

public interface ErrorType {
	
	
	byte ERR_TYPE_NORMAL = 0;
	byte ERR_TYPE_TRYCATCH = 1;
	
	
	
	byte ERR_GRP_UNKNOWN						= 0;
	byte ERR_GRP_METHOD_EXCEPTION 				= 1;
	byte ERR_GRP_JDBC_EXCEPTION	 				= 2;
	byte ERR_GRP_EXTERNALCALL_EXCEPTION 		= 3;
	byte ERR_GRP_APPLICATION_EXCEPTION 			= 4;

	byte JAVA_ERROR	 							= 5;
}

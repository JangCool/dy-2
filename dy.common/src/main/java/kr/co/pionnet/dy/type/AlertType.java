package kr.co.pionnet.dy.type;

public interface AlertType {
	
	int HTTP_404_ERROR 									= 1;
	int OUT_OF_MEMORY 									= 2;
	int JDBC_CONNECTION_UNCLOSED 					= 3;
	int JDBC_STATEMENT_UNCLOSED 						= 4;
	int JDBC_PREPAREDSTATEMENT_UNCLOSED 		= 5;
	int JDBC_CALLABLESTATEMENT_UNCLOSED 			= 6;
	int JDBC_RESULTSET_UNCLOSED 						= 7;
	int MEHTOD_EXCEPTION			 						= 8;
	int SQL_TOOMANY_FETCH			 					= 9;
	int EXTERNALCALL_EXCEPTION			 				= 10;
	int DB_CONNECTION_FAIL			 					= 11;
	int LONG_RESPONSE_TIME			 					= 12;
	int JAVA_ERROR						 					= 13;
}

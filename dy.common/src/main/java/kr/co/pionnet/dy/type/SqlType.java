package kr.co.pionnet.dy.type;

public interface SqlType {
	int STMT 										= 1;
	int PSTMT 										= 2;
	int PCALL 										= 3;
	int DBC_OPEN 									= 4;
	int EXEC 											= 5;
	int QUERY 										= 6;
	int UPDATE 										= 7;
	int FETCH 										= 8;
	int DBC_CLOSE 									= 9;
	int COMMIT	 									= 10;
	int SET_AUTO_COMMIT_TRUE	 			= 11;
	int SET_AUTO_COMMIT_FALSE	 			= 12;
	int BATCH	 									= 13;

}

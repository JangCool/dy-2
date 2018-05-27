package kr.co.pionnet.dy.type;

public interface DataType {

	/*public static String SERVICE="service"; 
	public static String REFERER="referer";
	public static String USERAGENT="useragent";
	public static String QUERYSTRING="querystring";
	public static String SQLTEXT="sqltext";
	public static String SQLPARAM="sqlparam";
	public static String PROFILE_H="pprofilehash";
	public static String PROFILE="profile";
	public static String TRACKER="tracker";
	public static String AGENT_INFO="AgentInfo";
	*/

	public static byte VIEW_CONNECT=1;
	public static byte AGENT_CONNECT=2;

	public static byte VIEW_CMD=3;
	public static byte AGENT_CMD=4;
	
	public static byte HEARTBEAT=5;

	public static byte TRACKER=20;
	public static byte PERFORMANCE=21;
	public static byte DB=22;
	public static byte APPSUMMARY=23;
	
	public static byte 	RECENTUNIQUSERS = 30;
	
	
	
	//TextBag
	public static byte SERVICE=10;
	public static byte QUERYSTRING=11;
	public static byte USERAGENT=12;
	public static byte REFERER=13;
	public static byte SQLTEXT=14;
	public static byte SQLPARAM=15;
	public static byte PROFILEHASH=16;
	public static byte PROFILE=17;	
	public static byte AGENTINFO=18;	
	public static byte ERROR=19;
	public static byte JDBCINFO=24;
	public static byte ERROR_ALERT=51;
	public static byte FILE_SYSTEM_DISK_USAGE_ALERT=52;
		
	
	
	//Performance Data Type
	final static public String TPS="TPS";
	final static public String TRS="TRS";
	final static public String ACTIVE="ACTIVE";		
	final static public String ASPEED = "ASPEED";
	final static public String OSUSEDCPU = "OSUSEDCPU";
	final static public String OSSYSCPU = "OSSYSCPU";
	final static public String OSUSERCPU = "OSUSERCPU";
	
	final static public String OSTOTALMEM = "OSTOTALMEM";
	final static public String OSFREEMEM = "OSFREEMEM";
	final static public String OSUSEDMEM = "OSUSEDMEM";
	final static public String OSMEMRATE = "OSMEMRATE";
	
	final static public String JVMMEMTOTAL = "JVMMEMTOTAL";
	final static public String JVMMEMFREE = "JVMMEMFREE";
	final static public String JVMMEMUSED = "JVMMEMUSED";
	
	final static public String AREAMEMORYUSAGE = "AREAMEMORYUSAGE";
	final static public String HEAPMEMORYUSAGE = "HEAPMEMORYUSAGE";
	final static public String NONEHEAPMEMORYUSAGE = "NONEHEAPMEMORYUSAGE";
	final static public String YOUNGAREAGCINFO = "YOUNGAREAGCINFO";
	final static public String OLDAREAGCINFO = "OLDAREAGCINFO";
	final static public String PROCESSCPU = "PROCESSCPU";
	
	final static public String DBMAXACTIVE = "DBMAXACTIVE";
	final static public String DBACTIVE = "DBACTIVE";
	final static public String DBIDLE = "DBIDLE";
	
	
	final static public String CONCURENT_USERS = "CONCURENT_USERS";
	
	final static public String TPS_AVR="TPS_AVR";
	final static public String TRS_ALL="TRS_ALL";
	final static public String TRS_AVR="TRS_AVR";
	final static public String ACTIVE_SUM = "ACTIVE_SUM";
	final static public String DB_CON_ACTIVE_SUM = "DB_CON_ACTIVE_SUM";
	
	final static public String GC_INFO = "GC_INFO";
	
}


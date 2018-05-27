package kr.co.pionnet.dy.type;

public interface CommandType {
	
	String KEY_TYPE = "type"; 
	String VAL_TYPE_SESSION = "session"; 

	String KEY_CONFIG_TYPE = "config_type"; 
	String KEY_AID = "aid";
	String KEY_DUMMY1 = "dummy1";
	String KEY_DUMMY2 = "dummy2";
	String KEY_DUMMY3 = "dummy3";
	String KEY_PROPERTY_KEY = "property_key"; 
	String KEY_MESSAGE = "message"; 
	String KEY_PATH = "path"; 
	String KEY_RESULT = "result"; 
	String KEY_WORK_TYPE = "work_type"; 
	
	String KEY_COLLECTOR_ID = "collector_id";
	
	String KEY_NET_CONTROLL_TOWER_ADD_ID_VIEW = "view_client"; 

	
	String VAL_PATH_XVIEW= "xview"; 
	String VAL_PATH_PERFORMANCE= "performance"; 
	String VAL_PATH_CONFIG= "config"; 
	String VAL_PATH_DB= "db"; 
	
	String KEY_SESSION_ID = "session_id"; 
	String KEY_START = "start"; 
	
	String KEY_WS_ID = "ws_id";
	
	String CMD_CO_HEARTBEAT= "HEARTBEAT"; 

	String CMD_OPEN = "OPEN"; 
	String CMD_XVIEW = "XVIEW"; 
	String CMD_PERFORMANCE = "PERFORMANCE"; 

	String CMD_CONFIG_LOAD = "CONFIG_LOAD"; 
	String CMD_CONFIG_SAVE  = "CONFIG_SAVE"; 
	String CMD_ACTIVE_LIST   = "ACTIVE_LIST"; 
	String CMD_ACTIVE_DETAIL   = "ACTIVE_DETAIL"; 
	String CMD_THREAD_INTERRUPT   = "THREAD_INTERRUPT"; 
	String CMD_THREAD_PRIORITY   = "THREAD_PRIORITY"; 
	String CMD_RESET_CACHE   = "RESET_CACHE"; 
	String CMD_LSN_CHECK = "LSN_CHECK";
	String CMD_SERVICE_DUMP = "SERVICE_DUMP";
	String CMD_THREAD_DUMP = "THREAD_DUMP";
	String CMD_DISABLE_AGENT = "DISABLE_AGENT";
	String CMD_ENABLE_AGENT = "ENABLE_AGENT";
	
	
	String CMD_DYNAMIC_CONFIG="DYNAMIC_CONFIG";
	String CMD_DYNAMIC_CONFIG_ALL="DYNAMIC_CONFIG_ALL";
	String CMD_DYNAMIC_CONFIG_ENABLE_AGENT="enable.agent";
	String CMD_DYNAMIC_CONFIG_PROFILE_CLASS_PATTERN="profile.class.pattern";
	String CMD_DYNAMIC_CONFIG_PROFILE_PACKAGE_PATTERN="profile.package.pattern";
	String CMD_DYNAMIC_CONFIG_PROFILE_METHOD_PATTERN="profile.method.pattern";
	String CMD_DYNAMIC_CONFIG_PROFILE_LINE_LIMIT="profile.line.limit";
	String CMD_DYNAMIC_CONFIG_PROFILE_EXCLUSION_URI="profile.exclusion.uri";
	String CMD_DYNAMIC_CONFIG_SEND_PROFILE_DATA_60K_AND_OVER="send.profile.data.60k_and_over";

	String CMD_DYNAMIC_CONFIG_ENABLE_JDBC_DRIVER_INFOMATION="enable.jdbc.driver.infomation";
	String CMD_DYNAMIC_CONFIG_JDBC_GET_CONNECTION_METHOD="jdbc.get.connection.method";
	
	String CMD_DYNAMIC_CONFIG_ACTIVE_SERVICE_COLOR_YELLOW_TIME="active.service.color.yellow.time";
	String CMD_DYNAMIC_CONFIG_ACTIVE_SERVICE_COLOR_RED_TIME="active.service.color.red.time";
	String CMD_DYNAMIC_CONFIG_ACTIVE_SERVICE_STACKTRACE_LINE_LIMIT="active.service.stacktrace.line.limit";

	String CMD_DYNAMIC_CONFIG_ERROR_EXCLUSION_EXCEPTION="error.exclusion.exception";
	String CMD_DYNAMIC_CONFIG_ENABLED_EXCEPTION_ROOT_CAUSE_TRACE="enabled.exception.root.cause.trace";
	String CMD_DYNAMIC_CONFIG_ENABLED_EXCEPTION_BEFORE_TRYCATCH_TRACE="enabled.exception.before.trycatch.trace";
	
	
	//dy.boot.config
	String BOOT_CONF_LOAD										= "BC0";  // dy.boot.config 프로퍼티 파일 읽어오기
	String BOOT_CONF_PROPERTY_SAVE	 					= "BC1";  // dy.boot.config 속성 값 변경 (단일 항목일 경우)
	String BOOT_CONF_PROPERTY_LIST_SAVE				= "BC2";  // dy.boot.config 속성 값 변경 (여러 항목일 경우)
	
	String AGENT_CONF_LOAD									= "AC0";  // agent.config 프로퍼티 설정.
	String AGENT_CONF_PROPERTY_SAVE	 					= "AC1";  // agent.config 속성 값 변경 (단일 항목일 경우)
	String AGENT_CONF_PROPERTY_LIST_SAVE				= "AC2";  // agent.config 속성 값 변경 (여러 항목일 경우)
	String AGENT_CONF_DYNAMIC_PROFILE_METHOD_SAVE		= "AC3";
	String AGENT_CONF_DYNAMIC_PROFILE_PACKAGE_SAVE		= "AC5";
	
	String VAL_RESULT_SUCCESS					= "S";  //통신 후 설정 작업이 정상적으로이루어 졌을 경우 
	String VAL_RESULT_SUCCESS_NONE		= "N";  //통신 후 설정 작업이 정상적으로이루어 졌으나 원하는 결과값이 없을 경우
	String VAL_RESULT_FAIL						= "F";  //통신 후 설정 작업이 정상적으로이루어 지지 않았을  경우 
	
	String CONFIG_AGENT_NOT_CONNECT	="C01";
	String CONFIG_DUP_CONNECT		="C02";

}

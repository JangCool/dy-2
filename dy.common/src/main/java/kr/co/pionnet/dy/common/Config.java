package kr.co.pionnet.dy.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.ConfigurationException;

import kr.co.pionnet.dy.exception.MatchedKeyNotFoundException;

public class Config {

    public static Configuration configuration = null;

    public String config_file;;
    
    static {
        if (configuration == null) {
            configuration = new Configuration();
        }
    }

	
	public static double getDouble(String key) {
        double value = 0.0;
        
        try {
			
	        try {
	            value = Double.parseDouble(configuration.get(key));
	        } catch (NumberFormatException e) {
	            if (!Boolean.parseBoolean(configuration.get(BaseConst.KEY_CONVERT_CONFIG_NULLSTRING_IGNORE))) {	// 기본값 설정 여부
	                throw e;
	            }
	        } catch (MatchedKeyNotFoundException e) {
	            if (!Boolean.parseBoolean(configuration.get(BaseConst.KEY_CONVERT_CONFIG_NULLSTRING_IGNORE))) {	// 기본값 설정 여부
	                throw e;
	            }
	        }
        
		} catch (Exception e) {
			e.printStackTrace();
		}
        return value;
    }

    public static double getDouble(String key, double defaultValue) {
        double value = 0.0;
        try {
            value = Double.parseDouble(configuration.get(key));
        } catch (NumberFormatException e) {
            value = defaultValue;
        } catch (MatchedKeyNotFoundException e) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * @param key
     * @return
     * @throws IOException
     * 
     *             프로퍼티 속성 조회. key에 해당하는 값이 없다면 convert.config.NullString.ignore 설정에 따라 기본값 리턴 결정
     * @throws ConfigurationException 
     */
    public static String getString(String key) {
        String value = "";	// 기본값
        try {
            value = configuration.get(key);
        } catch (Exception e) {
		}
        return value;
    }

    public static String getString(String key, String defaultValue) {
        String value = "";	// 기본값
        try {
            value = configuration.get(key);
        } catch (MatchedKeyNotFoundException e) {
            value = defaultValue;
        }
        return value;
    }

    public static String[] getStringArray(String key) {
        return getStringArray(key, ",");
    }

    public static String[] getStringArray(String key, String delimeter) {
    	
        String[] values = null;	// 기본값
        try {
            String value = configuration.get(key);
            values = value.split(delimeter);			
		} catch (Exception e) {
			e.printStackTrace();
		}

        return values;
    }

    public static short getShort(String key) {
        return (short) getDouble(key);
    }

    
    public static int getInt(String key){
        return (int) getDouble(key);
    }

    public static int getInt(String key, int defaultValue) {
        return (int) getDouble(key, defaultValue);
    }

    public static long getLong(String key) {
        return (long) getDouble(key);
    }

    public static long getLong(String key, long defaultValue){
        return (long) getDouble(key, defaultValue);
    }

    public static float getFloat(String key) {
        return (float) getDouble(key);
    }

    public static float getFloat(String key, float defaultValue) {
        return (float) getDouble(key, defaultValue);
    }

    public static boolean getBoolean(String key) {
        boolean value = Boolean.parseBoolean(configuration.get(key));
        return value;
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        boolean value = false;
        try {
            value = Boolean.parseBoolean(configuration.get(key));
        } catch (MatchedKeyNotFoundException e) {
            value = defaultValue;
        }
        return value;
    }
    
    public static void set(String key, String value) {
    	try {
			configuration.set(key, value);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
    }


}

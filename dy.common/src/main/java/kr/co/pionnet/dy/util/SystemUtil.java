package kr.co.pionnet.dy.util;

import java.io.PrintStream;
import java.util.Locale;

public class SystemUtil {
	
	public enum OS_TYPE {				
		AIX,
		HPUX,
		OS400,
		LINUX,
		MAC_OS,
		FREEBSD,
		OPENBSD,
		NETBSD,
		WINDOWS,
		SUN_OS,
		UNKNOWN, 
	}
	
	
    public static OS_TYPE getOsName() {
    	return getOsName(System.getProperty("os.name"));
    }
    public static OS_TYPE getOsName(String value) {
        value = normalize(value);
        if (value.startsWith("aix")) {
            return OS_TYPE.AIX;
        }
        if (value.startsWith("hpux")) {
            return OS_TYPE.HPUX;
        }
        if (value.startsWith("os400")) {
            // Avoid the names such as os4000
            if (value.length() <= 5 || !Character.isDigit(value.charAt(5))) {
                return OS_TYPE.OS400;
            }
        }
        if (value.startsWith("linux")) {
            return OS_TYPE.LINUX;
        }
        if (value.startsWith("macosx") || value.startsWith("osx")) {
            return OS_TYPE.MAC_OS;
        }
        if (value.startsWith("freebsd")) {
            return OS_TYPE.FREEBSD;
        }
        if (value.startsWith("openbsd")) {
            return OS_TYPE.OPENBSD;
        }
        if (value.startsWith("netbsd")) {
            return OS_TYPE.NETBSD;
        }
        if (value.startsWith("solaris") || value.startsWith("sunos")) {
            return OS_TYPE.SUN_OS;
        }
        if (value.startsWith("windows")) {
            return OS_TYPE.WINDOWS;
        }

        return OS_TYPE.UNKNOWN;
    }
    
    private static String normalize(String value) {
        return value.toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
    }
    
    public static void log(String work,String detail,String message){
    	
    	PrintStream out = null;
    	
    	if("FAIL".equals(work)){
    		out = System.err;
    	}else{
    		out = System.out;
    	}
    		
		out.println(TextUtil.concat("DRAGON-EYE[",detail,"]" ,work," - " ,message));
    }
    public static void log(String work,String message){
    	
    	PrintStream out = null;
    	
    	if("FAIL".equals(work)){
    		out = System.err;
    	}else{
    		out = System.out;
    	}
    	
		out.println(TextUtil.concat("DRAGON-EYE[",work,"]" ,message));
    }
    public static void log(String message){
		System.out.println(message);
    }
}

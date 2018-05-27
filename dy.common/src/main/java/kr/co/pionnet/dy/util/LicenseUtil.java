/*
 * ------------------------------------------------------------------------------
 * @Project       : license
 * @Source        : LicenseUril.java
 * @Description   : 
 * @Author        : l22sangdlf
 *------------------------------------------------------------------------------
 *                  변         경         사         항                       
 *------------------------------------------------------------------------------
 *    DATE     AUTHOR                      DESCRIPTION                        
 * ----------  ------  --------------------------------------------------------- 
 * 2017. 6. 2.   이상일    신규생성                                     
 *------------------------------------------------------------------------------
 */
/**   
* @Title: LicenseUril.java 
* @Package kr.co.pionnet.license 
* @Description: TODO(한 마디 말로 그 파일 뭐) 
* @author l22sangdlf 
* @date 2017. 6. 2. 오전 9:48:43 
* @version V1.0   
*/ 
package kr.co.pionnet.dy.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;

/** 
* @ClassName: LicenseUril 
* @Description: TODO
* @author l22sangdlf
* @date 2017. 6. 2. 오전 9:48:43 
*  
*/
public class LicenseUtil {
	
	//private static  int[] ARRAY_CHAR;	    
	//3GWYKLAHNPFJRTM5U7SV426EBQICZODX
	private static  int[] ENCODE_TABLE = {51,71,87,89,75,76,65,72,78,80,70,74,82,84,77,53,85,55,83,86,52,50,54,69,66,81,73,67,90,79,68,88};
	private static   String excludeChars = "0189abcdefghijklmnprstuvwxyz";

	private static HashMap<Character, Integer> CHAR_MAP;

    static final String SEPARATOR = "-";
    
    
    static LicenseUtil INSTANCE = null;
    
    public static LicenseUtil getInstance() {
    	if(INSTANCE == null) {
    		INSTANCE = new LicenseUtil();	
    	}
    	
    	return INSTANCE;
    }

    protected LicenseUtil() {

    	//this.ARRAY_CHAR = ENCODE_TABLE;
        
        CHAR_MAP = new HashMap<Character, Integer>();
        StringBuilder sb = new StringBuilder(); 
        for (int i = 0; i < ENCODE_TABLE.length; i++) {
            CHAR_MAP.put((char)ENCODE_TABLE[i], i);
            
            sb.append((char)ENCODE_TABLE[i]);
        }
        
    }
    
    
    
    public static long decode(String s) {
    	
        boolean isNegative = s.startsWith("-");
        int startIndex = isNegative ? 1 : 0;
        long base = 1;
        long result = 0;
        for (int i = s.length() - 1; i >= startIndex; i--) {
        	char c = s.charAt(i); 
        	
        	if(excludeChars.indexOf(String.valueOf(c)  ) == -1) {
        		int j = CHAR_MAP.get(c);
	            result = result + base * j;
	            base = base * 32;
        	} 
        }
        if (isNegative)
            result *= -1;
        
        return result;
    }
    
    
    public static boolean  isKeyStringValidate(String keyString) {
    		
    	if(keyString != null && (keyString.split(SEPARATOR).length == 5 || keyString.split(SEPARATOR).length == 6)) return true ;
    	else return false;
    	
    	
    	
    	
    }
   

	
    public  boolean checkValidTime(String keyString) {
    	
    	if(!isKeyStringValidate(keyString)) return false;
        String[] arrKey = keyString.split("-");        
      //  if(arrKey == null || arrKey.length == 5) return false;
        
        
        long seed =  getSeed( keyString);
        long issue = decode(arrKey[2]) ^ seed;
        long expired = decode(arrKey[3]) ^ issue ^ seed;
        long now = System.currentTimeMillis();
        
        
        
        
        return issue <= now && now <= expired;
    }
    
    private static long toLocalTimeMillis(long time) {
        TimeZone tz = TimeZone.getDefault();
        long offset = (long)tz.getRawOffset();
        if(tz.useDaylightTime()) {
            offset += 3600000L;
        }

        return time + offset;
    }
    
    private  long getSeed(String keyString) {
    	
    	String[] arrKey = keyString.split("-");
    	long limitCnt =  decode(arrKey[5])^decode(arrKey[4]) ;
    	
    	return decode(arrKey[4])-limitCnt;
    }
    private  long getServerIp(String keyString) {
    	
    	String[] arrKey = keyString.split("-");        
    	long seed = getSeed( keyString);
        long issue = decode(arrKey[2]) ^ seed;        
        long expired = decode(arrKey[3]) ^ issue ^ seed; 
        long lnSeverIp = decode(arrKey[1]) ^issue^ expired ^ seed;
        
        return lnSeverIp;
    }
    
    
    public  String expiredDt(String keyString) {
    	if(!isKeyStringValidate(keyString)) return ""; 
    	
    	String[] arrKey = keyString.split("-");        
    	long seed =getSeed( keyString);        
        long issue = decode(arrKey[2]) ^ seed;
        //System.out.println("issue : "+issue);
        long expired = decode(arrKey[3]) ^ issue ^ seed;
        //expired = expired*86400000L;
        //expired = toLocalTimeMillis(expired);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        
        
        return sdf.format(new Date(expired));
    }
    
    
    
    public  long getAgentIp(String keyString) {
    	
    	String[] arrKey = keyString.split("-");        
    	long seed = getSeed( keyString);      
    	long lnAgentIp = decode(arrKey[0]) ^getServerIp(keyString)^ seed;
        
        return lnAgentIp;    	
    }
    
    public String getStrAgentIp(String keyString) {
    	
    	  return convertIpToString(getAgentIp(keyString));
    }
    
    public boolean checkLsnMultiKey(String keyString) {
    	
    	boolean multiYn = false;
    	
    	if((getAgentIp(keyString)  == convertIpToLong("0.0.0.0")) && getLicenseLimitCount(keyString) > 1) {    		
    		multiYn = true;    		
    	} 
    	
    	return multiYn;
    			
    }
    	
    
    private  long getLicenseLimitCount(String keyString) {
    	String[] arrKey = keyString.split("-");        
    	long limitCnt =  decode(arrKey[5])^decode(arrKey[4]) ;
    	
    	
    	return limitCnt;
    }
    	
	
	public  long convertIpToLong( String ip ) {
		String sIpNo = ip;
		
		// "."으로 잘라서 리스트에 담는다.
		StringTokenizer st = new StringTokenizer( sIpNo, "." );
		List lIp = new ArrayList();
		
		while( st.hasMoreElements() ){
			lIp.add( st.nextElement() );
		}

		int iNo = 0;		//리스트의 값을 담을 변수
		final int iref = lIp.size()-1;	//
		int iNcount = 0;			//256의 제곱변수
		long lResult = 0;			//리스트의 값과 256의 제곱근의 합을 담는 변수
		
		for( int i = 0; i < lIp.size(); i++ ){
			iNcount = iref - i;		//제곱할 수
			iNo = Integer.parseInt( lIp.get(i).toString() );	//리스트의 값을 담는다.
			lResult += iNo*((long)(Math.pow(256, iNcount)));	//
		}
		
		return lResult;
	}
	
	
	public  String convertIpToString( long lIpAddress){
		
		long lIpNo = lIpAddress;
		StringBuffer sbIp = new StringBuffer();
		
		if( lIpNo > lIpAddress ){
			System.out.println("잘못된 코드입니다.");
		}else{
			List lIpResult = new ArrayList();
			int iN1;
			int iN2;
			int iN3;
			int iN4;
			int iResult;
			
			iN1 = (int) (lIpNo/256);		// 000.000.000.XXX : XXX를 구함
			iResult = (int) (lIpNo%256);
			lIpResult.add(iResult);
			
			iN2 = iN1/256;					// 000.000.XXX.000 : XXX를 구함
			iResult = iN1%256;
			lIpResult.add(iResult);
			
			iN3 = iN2/256;					// 000.XXX.000.000 : XXX를 구함
			iResult = iN2%256;
			lIpResult.add(iResult);
			
			iN4 = iN3/256;					// XXX.000.000.000 : XXX를 구함
			iResult = iN3%256;
			lIpResult.add(iResult);
			
			
			for( int k = lIpResult.size()-1; k >= 0; k-- ){
				sbIp.append( lIpResult.get(k) );
				if( k == 0 ){
					//
				}else{
					sbIp.append( "." );
				}
			}
			//System.out.println( "IP : "+ sbIp.toString() ); 
		}
		
		return sbIp.toString();
		
	}
	
	
	public  boolean validate(String keyString, String aIp, String sIp) {
		return validate(keyString, aIp, sIp, 1);
	}
	
	public  boolean validate(String keyString, String aIp, String sIp, long limit) {		
		if(!isKeyStringValidate(keyString)) return false; 
		
		if("0.0.0.0".equals(convertIpToString(getAgentIp(keyString)))) {
			
			return   (getServerIp(keyString) == convertIpToLong(sIp)) && checkValidTime(keyString) && getLicenseLimitCount(keyString) >= limit ;
		} else {
			return (getAgentIp(keyString) == convertIpToLong(aIp)) &&   (getServerIp(keyString) == convertIpToLong(sIp)) && checkValidTime(keyString); 
		}
		
		
	}
	
	
	public  String getResultValidate(String keyString, String aIp, long limit) {		
		
		if(keyString == null || "".equals(keyString)) return "F";		
		
		if(!checkValidTime(keyString)) {			
			return "E";
		} else if(getLicenseLimitCount(keyString) < limit ) {
			return "F";	
		} else if(getServerIp(keyString) != convertIpToLong(NetUtil.getLocalServerIp())){
			return "F";
		} else if(!"0.0.0.0".equals(convertIpToString(getAgentIp(keyString))) && !aIp.equals(convertIpToString(getAgentIp(keyString)) )) {			
			return "F";
		} else {
			return "T";
		}		
			
	}
	
	

	
}



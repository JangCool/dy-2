/*
 * ------------------------------------------------------------------------------
 * @Project       : dy_db
 * @Source        : SearchSubCondition.java
 * @Description   : 
 * @Author        : l22sangdlf
 *------------------------------------------------------------------------------
 *                  변         경         사         항                       
 *------------------------------------------------------------------------------
 *    DATE     AUTHOR                      DESCRIPTION                        
 * ----------  ------  --------------------------------------------------------- 
 * 2017. 5. 15.   이상일    신규생성                                     
 *------------------------------------------------------------------------------
 */
/**   
* @Title: SearchSubCondition.java 
* @Package kr.co.pionnet.dragon.db 
* @Description: TODO(한 마디 말로 그 파일 뭐) 
* @author l22sangdlf 
* @date 2017. 5. 15. 오후 2:28:50 
* @version V1.0   
*/ 
package kr.co.pionnet.dragon.db;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
* @ClassName: SearchSubCondition 
* @Description: TODO
* @author l22sangdlf
* @date 2017. 5. 15. 오후 2:28:50 
*  
*/
public class SearchSubCondition {
	
	public List aid;
	public int serviceHash = -1;
	public String c_ip;
	
	public boolean errCheck = false;  //여러 검색 여부
	
	public long startTime = -1;
	public long endTime =   -1;
	
	public long minElapsed =  -1;  //최소응담시간
	public long sElapsed = -1;
	public long eElapsed = -1;
	
	public long sSqlTime = -1;
	public long eSqlTime = -1;
	
	public long sCpuTime = -1;
	public long eCpuTime = -1;
	
	public int err_cls = -1;
	
	public boolean isExcludeFilter;
	public List filterValues;

	
	
	public static void main(String[] args) {
	
		String line = "211.108.23.11";
		
		
		System.out.println(line.matches("211.108.23.11 "));
		
		
		/*Pattern p = Pattern.compile("211.108.*.*");
		Matcher m  = p.matcher(line);
		int count = 0;
		while(m.find()) {
	         count++;
	         System.out.println("Match number "+count);
	         System.out.println("start(): "+m.start());
	         System.out.println("end(): "+m.end());
	      }*/

	}
	
	
	
}




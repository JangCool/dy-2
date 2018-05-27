/*
 * ------------------------------------------------------------------------------
 * @Project       : dy_db
 * @Source        : DBTest.java
 * @Description   : 
 * @Author        : l22sangdlf
 *------------------------------------------------------------------------------
 *                  변         경         사         항                       
 *------------------------------------------------------------------------------
 *    DATE     AUTHOR                      DESCRIPTION                        
 * ----------  ------  --------------------------------------------------------- 
 * 2017. 2. 28.   이상일    신규생성                                     
 *------------------------------------------------------------------------------
 */
/**   
* @Title: DBTest.java 
* @Package kr.co.pionnet.dragon.db 
* @Description: TODO(한 마디 말로 그 파일 뭐) 
* @author l22sangdlf 
* @date 2017. 2. 28. 오전 10:07:16 
* @version V1.0   
*/ 
package kr.co.pionnet.dragon.db;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import kr.co.pionnet.dy.util.DateUtil;
import kr.co.pionnet.dy.util.FileUtil;

/** 
* @ClassName: DBTest 
* @Description: TODO
* @author l22sangdlf
* @date 2017. 2. 28. 오전 10:07:16 
*  
*/
public class DBTest {

	
	public static int getDiffDayCount(String fromDate, String toDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		try {
			return (int) ((sdf.parse(toDate).getTime() - sdf.parse(fromDate).getTime()) / 1000 / 60 / 60 / 24);
		} catch (Exception e) {
		
		}
		
		return 0;
	}
	
	
	public static float getCount5MinuteCount(long fromDate, long toDate)  {
		long diff = toDate-fromDate;
		int mod =(int) (diff / 1000 %60);		
		if(mod >= 0) {
			return (float)(diff / 1000.0 / 60./ 5.)+1 ;
		}else {
			return (float)(diff / 1000.0 / 60./ 5.) ;
		}
	}
	
	
	public static List searchFileListOfRange(long from, long to) {
								
		List fileList = new ArrayList();
		int length = (int)Math.round(getCount5MinuteCount(from,to));
		    
	    
	    String type = "tracker";		
		String fileHome= "D:\\project\\dragon-eye\\databases";
		
		int cnt = 0;
		for(int i = 0;i <=  length;i++) {
			long startTime =new Date(DateUtil.getMinute(from, i*5)).getTime();			
			String startPath = FileUtil.makeDBFilePath(fileHome, "tracker",  startTime, TimeUnit.MINUTES, 5 );
			String endPath = FileUtil.makeDBFilePath(fileHome, "tracker", to, TimeUnit.MINUTES, 5 );

			if(startPath.equals(endPath)) {
				System.out.println("11111"+FileUtil.makeDBFilePath(fileHome, "tracker",  startTime, TimeUnit.MINUTES, 5 ));
				break;
			} else  {			
				if(i == length) {
					startTime = to;	
				} 
					System.out.println("222"+FileUtil.makeDBFilePath(fileHome, "tracker",  startTime, TimeUnit.MINUTES, 5 ));
				
				if(to - startTime < 0) {
					break;
				}
			}
		}
		return fileList;
		
		
		
	}
	

	
	
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2017);
		cal.set(Calendar.MONTH, Calendar.APRIL);
		cal.set(Calendar.DATE, 10);
		cal.set(Calendar.HOUR_OF_DAY, 11);
		cal.set(Calendar.MINUTE, 29);
		cal.set(Calendar.SECOND, 10);
		
		
		Calendar cal1 = Calendar.getInstance();
		cal1.set(Calendar.YEAR, 2017);
		cal1.set(Calendar.MONTH, Calendar.APRIL);
		cal1.set(Calendar.DATE, 10);
		cal1.set(Calendar.HOUR_OF_DAY, 11);
		cal1.set(Calendar.MINUTE, 40);
		cal1.set(Calendar.SECOND, 00);
		cal1.set(Calendar.MILLISECOND, 00);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		String fromDate = sdf.format(new Date(cal.getTime().getTime()));
		String toDate = sdf.format(new Date(cal1.getTime().getTime()));

		
		
		searchFileListOfRange(cal.getTime().getTime(), cal1.getTime().getTime());
		//getCount5MinuteCount(fromDate, toDate);

		/*List<String> list = searchFileListOfRange(cal.getTime().getTime(), cal1.getTime().getTime());
		
		System.out.println("szie ===>"+list.size());
		
		for(String f:list) {
			System.out.println(f);
		}*/
	
	}
	
}



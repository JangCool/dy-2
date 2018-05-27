/*
 * ------------------------------------------------------------------------------
 * @Project       : dy_db
 * @Source        : DBFileInfo.java
 * @Description   : 
 * @Author        : l22sangdlf
 *------------------------------------------------------------------------------
 *                  변         경         사         항                       
 *------------------------------------------------------------------------------
 *    DATE     AUTHOR                      DESCRIPTION                        
 * ----------  ------  --------------------------------------------------------- 
 * 2017. 4. 12.   이상일    신규생성                                     
 *------------------------------------------------------------------------------
 */
/**   
* @Title: DBFileInfo.java 
* @Package kr.co.pionnet.dragon.db 
* @Description: TODO(한 마디 말로 그 파일 뭐) 
* @author l22sangdlf 
* @date 2017. 4. 12. 오후 5:29:17 
* @version V1.0   
*/ 
package kr.co.pionnet.dragon.db;
 
/** 
* @ClassName: SearchCondition 
* @Description: TODO
* @author l22sangdlf
* @date 2017. 4. 12. 오후 5:29:17 
*  
*/
public class SearchCondition {

	public String searchFilePath;
	public long tx_id;
	public long fromTime;
	public long toTime;	
	public boolean isFirst = false;
	public boolean isLast = false;
	
	public SearchSubCondition subCondition;
	
	/**
	 * @param searchFilePath
	 * @param fromTime
	 * @param toTime
	 * @param elapsed
	 */
	public SearchCondition(String searchFilePath,  boolean isFirst, boolean isLast, long fromTime, long toTime, SearchSubCondition subCondition) {
		
		this.searchFilePath = searchFilePath;
		this.fromTime = fromTime;
		this.toTime = toTime;
	
		this.isFirst = isFirst;
		this.isLast = isLast;		
		this.subCondition = subCondition;
	}
	
	
	public SearchCondition(String searchFilePath,  long tx_id, boolean isFirst, boolean isLast, long fromTime, long toTime) {
		
		this.searchFilePath = searchFilePath;
		this.tx_id = tx_id;
		this.fromTime = fromTime;
		this.toTime = toTime;		
		this.isFirst = isFirst;
		this.isLast = isLast;
	}
	
	public void print() {
		System.out.println("fromTime :"+fromTime);
		System.out.println("toTime :"+toTime);
	
		System.out.println("isFirst :"+isFirst);
		System.out.println("isLast :"+isLast);
		
	}
	
}



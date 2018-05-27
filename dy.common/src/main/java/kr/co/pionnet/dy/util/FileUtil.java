package kr.co.pionnet.dy.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import kr.co.pionnet.dy.exception.AgentException;


public class FileUtil
{
	

	public FileUtil()
	{
	}
	
	/**
	 * 경로 가져오기
	 * @param strFullPath 경로
	 * @return
	 */
	public static String getPath(String strFullPath)
	{
		if(strFullPath == null || strFullPath.length() < 1)
		{
			return strFullPath;
		}
		
		int nPosLast = strFullPath.lastIndexOf( "/" );
		
		// 구분자 '/' 없고, 확장자가 없을 경우
		if( nPosLast == -1 && strFullPath.indexOf( ".") == -1) 
			return strFullPath;
		
		return strFullPath.substring( 0, nPosLast );
	}

	/**
	 * 디렉토리 생성하기
	 * 
	 * @param strDirectoryPath
	 *            생성할 디렉토리명
	 * @return 생성 성공 : true, 실패 : false
	 */
	public static boolean createDirectory(String strDirectoryPath)
	{
		boolean retValue = false;
		
		retValue = new File( strDirectoryPath ).mkdirs();
		
		return retValue;
	}
	


	/**
	 * 지정한 경로 값의 디렉토리 여부
	 * 
	 * @param strFileName
	 *            경로
	 * @return 디렉토리일 경우 true, 아닐 경우 false
	 */
	public static boolean isDirectory(String strFileName)
	{
		boolean retValue = false;
		try
		{
			retValue = new File( strFileName ).isDirectory();
		}
		catch( Exception e )
		{
			e.printStackTrace();
			retValue = false;
		}
		return retValue;
	}
	


	/**
	 * 파일 또는 디렉토리가 한글일 경우 인코딩 처리하기
	 * 
	 * @param sString
	 *            파일명 또는 디렉토리명
	 * @return 인코딩 처리된 파일명 또는 디렉토리명
	 */
	private static String toEncoding(String strString)
	{
		String retValue = null;
		try
		{
			retValue = new String( strString.getBytes( "8859_1" ), "KSC5601" );
		}
		catch( Exception e )
		{
			e.printStackTrace();
			retValue = strString;
		}
		return retValue;
	}
	


	/**
	 * 파일 또는 디렉토리 존재 여부
	 * 
	 * @param strFileName
	 *            파일명 또는 디렉토리명
	 * @return 존재한다고면 true, 존재하지 않는다면 false
	 */
	public static boolean isExists(String strFileName)
	{
		File objFile = new File( strFileName );
		
		return objFile.exists();
	}
	


	/**
	 * 파일 또는 디렉토리 삭제하기
	 * 
	 * @param strFileName
	 *            삭제할 파일 또는 디렉토리
	 * @param bChild
	 *            하위 디렉토리 및 파일 삭제 여부
	 * @return
	 */
	public static boolean delete(String strFileName, boolean bChild)
	{
		
		File objFile = new File( strFileName );
		
		try
		{
			if ( !objFile.exists() )
			{ // 파일 또는 디렉토리가 존재하지 않는다면
				return false;
			}
			

			if ( objFile.delete() )
			{
				return true;
			}
			else if ( objFile.isDirectory() )
			{
				if ( !bChild )
				{
					return false;
				}
				
				if ( !strFileName.substring( strFileName.length() - 1 ).equals( "/" ) )
				{
					strFileName = strFileName + "/";
				}
				
				boolean bDelete = true;
				

				
				/***********************************************************************************
				 * 하위 폴더는 존재하지 않는다는 가정하에 디렉토리를 삭제한다.
				 **********************************************************************************/
				String[] arsFileList = objFile.list();
				
				for (int i = 0 ; i < arsFileList.length ; i++)
				{
					
					if ( !delete( strFileName + arsFileList[i] ) )
					{
						if ( bDelete )
						{
							bDelete = false;
						}
					}
				}
				
				if ( bDelete )
				{
					bDelete = objFile.delete();
				}
				
				return bDelete;
			}
			else
			{
				return false;
			}
			
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return false;
		}
	}
	



	/**
	 * 파일 또는 디렉토리 삭제하기
	 * 
	 * @param sFileName 삭제 파일명 또는 디렉토리
	 * @return
	 */
	public static boolean delete(String strFileName)
	{
		File objDeleteFile = new File( strFileName );
		
		return objDeleteFile.delete();
	}
	

	/**
	 * 파일 또는 디렉토리  이동하기 
	 * 
	 * @param orgPath 원본 경로
	 * @param destPath 이동할 경로
	 * @return
	 */
	public static boolean move(String orgPath ,String destPath)
	{
		File file = new File(orgPath);
		File fileToMove = new File(destPath);

		boolean isMoved = file.renameTo(fileToMove);
		
		return isMoved;
	}
	
	

	/**
	 * 파일명 또는 디렉토리명 변경하기
	 * 
	 * @param strSrc 기존 파일명 또는 디렉토리명
	 * @param strDest 변경될 파일명 또는 디렉토리명
	 * @return 변경 성공 여부 
	 */
	public static boolean rename(String strSrc, String strDest)
	{
		boolean retValue = false;
		
		File objSrcFile = new File( toEncoding( strSrc ) ); // 파일 또는 디렉토리가 한글 일 경우
		File objDestFile = new File( toEncoding( strDest ) ); // 파일 또는 디렉토리가 한글 일 경우
		
		try
		{
			retValue = objSrcFile.renameTo( objDestFile );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return retValue;
	}
	
	public static List<String> readLine(String filePath){
		return readLine(filePath,System.getProperty("file.encoding"));
	}

	public static List<String> readLine(String filePath,String charset){

		List<String> readLine =  new ArrayList();
		
		if(!isExists(filePath)){
			throw new AgentException("[DRAGON-EYE] 읽을 파일이 존재 하지 않습니다.");
		}
		
		FileInputStream fis= null;
		BufferedInputStream bis= null;
		try {
			
			fis = new FileInputStream(filePath);
			bis = new BufferedInputStream(fis);

	        Scanner s = new Scanner( bis , charset);
	        while ( s.hasNext( ) ) {
	        	readLine.add(s.nextLine( ));
	        }
	        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return readLine;
	}
	
	
    /** text로 File에 기록한다. */	 
    public static void write(String str, String filePath) throws Exception {
    	
    	BufferedWriter out  = null;

    	try {
    		
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
            out.write(str);
            out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
    }
    
    /** file에 설정된 특정 키의 값을 업데이트한다. (agent_id=xxx) */
    public static void updateConfValue(String filePath, String key, String value){
    	
	        String text = "";
			
			FileInputStream fis= null;
			BufferedInputStream bis= null;
			
			try{			
				fis = new FileInputStream(filePath);
				bis = new BufferedInputStream(fis);
	
				boolean targetChk = false;
		        Scanner s;
				try {
					s = new Scanner((Readable) new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
					while(s.hasNext()){	   
			        	String line = s.nextLine();
			        	if(!targetChk && line.indexOf(key) != -1){
			        		text += key + "=" + value + "\r\n";
			        		targetChk = true;
			        	}else{
			        		text += line + "\r\n";
			        	}
			        }	
				}catch (UnsupportedEncodingException e){
					e.printStackTrace();
				}		                
			}catch (FileNotFoundException e){
				e.printStackTrace();
			}
        
	    	try{  
		       BufferedWriter buffWrite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
		       
		       buffWrite.write(text);    
		       buffWrite.flush();  
		       buffWrite.close();  
		   }catch (Exception e){  
			   e.printStackTrace();
		   }  
    }
    
    /** file에 설정된 키와 값을 map 형식으로 리턴한다. */
    public static Map<String,String> readConfValue(String filePath){
    	
    	Map<String,String> map = new HashMap<String, String>();
    	
    	if(!isExists(filePath)){
//			throw new AgentException("[DRAGON-EYE] 읽을 파일이 존재 하지 않습니다.");
    		return null;
		}else{
	    	FileInputStream fis= null;
			BufferedInputStream bis= null;
			try {
				
				fis = new FileInputStream(filePath);
				bis = new BufferedInputStream(fis);

		        Scanner s;
				try {
					s = new Scanner((Readable) new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
					
					while(s.hasNext()){
			        	String[] line = s.nextLine().split("=");			        	
			        	map.put(line[0],line[1]);
			        }
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}    	
	    	
	    	return map;
		}
	}    
	
    /** InputStream으로 File에 기록한다. */	 
    public static void write(InputStream in, String filePath) {
    	
    	File file = new File(filePath);
    	write(in, file, 1024);
    }    
    
    /** InputStream으로 File에 기록한다. */	 
    public static void write(InputStream in, String filePath,int byteLength) {
    	
    	File file = new File(filePath);
    	write(in, file, byteLength);
    }     
    
    /** InputStream으로 File에 기록한다. */	 
    public static void write(InputStream in, File file) {
    	write(in, file, 1024);
    }       
    
    /***
     *  바이트 길이 지정 하여 파일을 쓴다. 
     * @param in
     * @param file
     * @param byteLength
     */
    public static void write(InputStream in, File file,int byteLength) {

        FileOutputStream os = null;
        BufferedOutputStream bos = null; 
        
        try {

        	os = new FileOutputStream(file);
            bos = new BufferedOutputStream(os);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = in.read(bytes)) != -1) {
                    os.write(bytes, 0, c);
            }

        } catch (FileNotFoundException e) {

        	e.printStackTrace();
        } catch (IOException e) {

        	e.printStackTrace();

        } finally {

            try {
            	if (os != null){os.close();}
            } catch (IOException e) {
            	e.printStackTrace();
            }
        }
    }


	public static void write(File file, byte[] byteArray)  {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(byteArray);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public static void close(InputStream in){
		try {
			if (in != null) {
				in.close();
			}
		} catch (IOException ex) {
		}
	}
	
	public static void close(OutputStream out){
		try {
			if (out != null) {
				out.close();
			}
		} catch (IOException ex) {
		}
	}
	
	
	public static String makeDBFilePath(String fileHome,    String fileType,  long createTime, TimeUnit timeUnit,  int period) {
		
		return getDBFilePath(fileHome, fileType, createTime, timeUnit, period, 0);
	}
	/**
	 * 
	  * @Method Name : makeDBFilePath
	  * @작성일 : 2016. 12. 26.
	  * @작성자 : 2sangdlf
	  * @변경이력 : 
	  * @Method 설명 : DB 파일의  Path를  생성 
	  * @param fileHome : 파일 생성되는 Home	  
	  * @param timeUnit : 파일 생성 범위
	  * @param createTime : 파일 생성  time
	  * @param  fileType : 파일 타입
	  * @return 파일full path
	 */
	public static String getDBFilePath(String fileHome,    String fileType,  long createTime, TimeUnit timeUnit,  int period, int aMinute) {
				
		String currentFormatTime, toDay, hour, minutes;		
		String dbFilePath = "";
		switch (timeUnit) {
		case HOURS:
			currentFormatTime = DateUtil.format(new Date(createTime - aMinute*60*1000), "yyyyMMddHH");
			toDay = currentFormatTime.substring(0, 8);
			hour =  currentFormatTime.substring(8, 10);			
			dbFilePath = fileHome.concat("/").concat(fileType).concat("/").concat(toDay).concat("/").concat(hour).concat("/").concat(fileType);
			break;
			
		case DAYS://일단위
			currentFormatTime = DateUtil.format(new Date(createTime - aMinute*60*1000), "yyyyMMdd");
			toDay = currentFormatTime.substring(0, 8);				 	
			dbFilePath = fileHome.concat("/").concat(fileType).concat("/").concat(toDay);
			break;
			
		case MINUTES:  //5분단위
			currentFormatTime = DateUtil.format(new Date(createTime - aMinute*60*1000) , "yyyyMMddHHmm");
			toDay = currentFormatTime.substring(0, 8);
			hour =  currentFormatTime.substring(8, 10);
			minutes =  currentFormatTime.substring(10, 12);
			int intMin =Integer.parseInt(minutes);
			
			String fileDir = fileHome.concat("/").concat(fileType).concat("/").concat(toDay).concat("/").concat(hour);
			if(period == 5 ) {
		
				
				if(intMin >= 0 && intMin <5) {
					dbFilePath = fileDir.concat("/").concat(fileType).concat("_").concat("00").concat("M");
				} else if(intMin >= 5 && intMin < 10) {
					dbFilePath = fileDir.concat("/").concat(fileType).concat("_").concat("05").concat("M");
				} else if(intMin >= 10 && intMin < 15) {
					dbFilePath = fileDir.concat("/").concat(fileType).concat("_").concat("10").concat("M");
				} else if(intMin >= 15 && intMin < 20) {
					dbFilePath = fileDir.concat("/").concat(fileType).concat("_").concat("15").concat("M");
				} else if(intMin >= 20 && intMin <25) {
					dbFilePath = fileDir.concat("/").concat(fileType).concat("_").concat("20").concat("M");
				} else if(intMin >= 25 && intMin <30) {
					dbFilePath = fileDir.concat("/").concat(fileType).concat("_").concat("25").concat("M");
				} else if(intMin >= 30 && intMin <35) {
					dbFilePath = fileDir.concat("/").concat(fileType).concat("_").concat("30").concat("M");
				} else if(intMin >= 35 && intMin <40) {
					dbFilePath = fileDir.concat("/").concat(fileType).concat("_").concat("35").concat("M");
				}  else if(intMin >= 40 && intMin <45) {
					dbFilePath = fileDir.concat("/").concat(fileType).concat("_").concat("40").concat("M");
				}  else if(intMin >= 45 && intMin <50) {
					dbFilePath = fileDir.concat("/").concat(fileType).concat("_").concat("45").concat("M");
				}  else if(intMin >= 50 && intMin <55) {
					dbFilePath = fileDir.concat("/").concat(fileType).concat("_").concat("50").concat("M");
				}  else if(intMin >= 55 && intMin <=59) {
					dbFilePath = fileDir.concat("/").concat(fileType).concat("_").concat("55").concat("M");
				}
			
			} else if(period == 10) {
				if(intMin >= 0 && intMin < 10) {
					dbFilePath = fileDir.concat("/").concat(fileType).concat("_").concat("00").concat("M");
				} else if(intMin >= 10 && intMin < 20) {
					dbFilePath = fileDir.concat("/").concat(fileType).concat("_").concat("10").concat("M");
				} else if(intMin >= 20 && intMin < 30) {
					dbFilePath = fileDir.concat("/").concat(fileType).concat("_").concat("20").concat("M");
				} else if(intMin >= 30 && intMin < 40) {
					dbFilePath = fileDir.concat("/").concat(fileType).concat("_").concat("30").concat("M");
				} else if(intMin >= 40 && intMin < 50) {
					dbFilePath = fileDir.concat("/").concat(fileType).concat("_").concat("40").concat("M");
				} else if(intMin >= 50 && intMin <= 59) {
					dbFilePath = fileDir.concat("/").concat(fileType).concat("_").concat("50").concat("M");
				}
			}
			
		default:
			break;
		}
		
		return dbFilePath;
	}

}

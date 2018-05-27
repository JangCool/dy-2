/*
 * ------------------------------------------------------------------------------
 * @Project       : dy_common
 * @Source        : CompressionUtil.java
 * @Description   : 
 * @Author        : l22sangdlf
 *------------------------------------------------------------------------------
 *                  변         경         사         항                       
 *------------------------------------------------------------------------------
 *    DATE     AUTHOR                      DESCRIPTION                        
 * ----------  ------  --------------------------------------------------------- 
 * 2017. 2. 14.   이상일    신규생성                                     
 *------------------------------------------------------------------------------
 */
/**   
* @Title: CompressionUtil.java 
* @Package kr.co.pionnet.dragon.util 
* @Description: TODO(한 마디 말로 그 파일 뭐) 
* @author l22sangdlf 
* @date 2017. 2. 14. 오후 2:24:35 
* @version V1.0   
*/ 
package kr.co.pionnet.dy.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.xerial.snappy.Snappy;

/** 
* @ClassName: CompressionUtil 
* @Description: TODO
* @author l22sangdlf
* @date 2017. 2. 14. 오후 2:24:35 
*  
*/
public class CompressionUtil {
	
	static  List<String>  deleteFileList = new ArrayList<String>();

	public static void compression(String orgFile) {
		try {
			if(FileUtil.isExists(orgFile)) {
				byte[] compressBytes = Snappy.compress(read(orgFile));
				write(compressBytes, orgFile.concat(".compressed"));
				File file = new File(orgFile.concat(".compressed"));
				if(file.exists()) {
					new File(orgFile).delete();
				}			
			}
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	public static void unCompression(String commpressedFilePath, boolean commpressedFileDeleteYn) {
		try {
			
			if(FileUtil.isExists(commpressedFilePath)) {
				byte[] deCompressByte = Snappy.uncompress(read(commpressedFilePath));			
				String destFile = commpressedFilePath.substring(0,commpressedFilePath.indexOf(".compressed"));
				
				write(deCompressByte, destFile);
				File file = new File(destFile);
				if(file.exists()) {
					
					if(commpressedFileDeleteYn)		new File(commpressedFilePath).delete();
					
				}			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	
	private static byte[] read(String fileName)  {		
		ByteBuffer buffer = null;
		RandomAccessFile aFile = null;
		FileChannel inChannel = null;
		byte[] bytes = null;
		try {
            aFile = new RandomAccessFile(fileName, "r");
            inChannel = aFile.getChannel();
            
            buffer = ByteBuffer.allocate((int) inChannel.size());
            aFile.seek(0);            
            aFile.read(buffer.array());
            
        } catch (Exception ioe) {
            ioe.printStackTrace();
        } finally {
            buffer.clear(); // do something with the data and clear or compact it.
            try {
				inChannel.close();  
				aFile.close();
	            
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         
        }
         
         return buffer.array();
	}
	
	
	private static void write(byte[] bytes, String fileName)  {
		RandomAccessFile aFile = null;
		FileChannel outChannel = null;
		MappedByteBuffer buffer = null;
		try {
		 	
			aFile = new RandomAccessFile(fileName, "rw");
			outChannel = aFile.getChannel();
			
			buffer = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, bytes.length);
			buffer.put(bytes);
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			 buffer.clear(); // do something with the data and clear or compact it.
	            try {
	            	outChannel.close();  
					aFile.close();
		            
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        
		}
		
	}
	
	
	
	public static void add(String path) {
		deleteFileList.add(path);
		
		//System.out.println("========>>>"+deleteFileList.size());
	}
	
	
	
	
	
	
	
}



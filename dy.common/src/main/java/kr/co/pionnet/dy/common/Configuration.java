package kr.co.pionnet.dy.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.naming.ConfigurationException;

import kr.co.pionnet.dy.exception.MatchedKeyNotFoundException;
import kr.co.pionnet.dy.util.TextUtil;

public class Configuration {

    protected Properties properties;
    protected Properties systemProperties;
    

    public Configuration() {
        load();
    }

    
    protected void load() {      
    	load(null);
    }
    
    
    public void load(String path) {      

    	String runtimeJarConfig = null;

        try {
        	if(TextUtil.isEmpty(path)){
            	runtimeJarConfig = "conf/dy.boot.conf";

        	}else{
        		runtimeJarConfig = path;
        	}
        	if(properties == null){
            	properties = new Properties();        		
        	}
			loadProperties(runtimeJarConfig);				
			
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
           
        }
    }
    
 
    
    private InputStream getPropertiesInputStream(String file_name) throws IOException{
    	
    	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = null;

        if (file_name != null) {
            File file = new File(file_name);
            if (!file.exists()) {// 해당 properties파일이 경로에 없다면 classpath에서 찾는다.
                URL url = classLoader.getResource(file_name);                
                if (url != null) {// classpath상에 properties 파일이 존재할 경우
                    is = classLoader.getResourceAsStream(file_name);
                }
            } else {
                is = new FileInputStream(file);
            }
        }
        
       return is;

    }
    
    private void loadProperties(String file_name) throws IOException{
    	InputStream is = getPropertiesInputStream(file_name);
    	if(is != null){
    		properties.load(is);
    	}
    }
    
    
    
    public String get(String key) throws MatchedKeyNotFoundException{
    	
    	String value = "";
    	
	
    	try {
            if (properties == null) {
                throw new ConfigurationException("properties가 존재하지 않습니다.");
            }			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	if(properties != null){

            value = properties.getProperty(key);
            
            if(TextUtil.isEmpty(value)){
            	value = System.getProperty(key);
            }
            
            if (value == null) {
                throw new MatchedKeyNotFoundException("해당 key 값이 존재 하지 않습니다.".concat("[").concat(key).concat("]"));
            }
            value = value.trim();
    	}
    	
        return value;
    }
    
    public void set(String key,String value) throws ConfigurationException {
        
        if(TextUtil.isEmpty(key)){
        	 throw new ConfigurationException("key 값이 존재하지 않습니다.");
        }
        
        properties.setProperty(key, value);
        
    }
    

    
    
    
}

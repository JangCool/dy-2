package kr.co.pionnet.dy.service.sample;

import java.util.List;
import java.util.Map;

public interface SampleService {

	
	public List<Map<String,?>> getDual()throws Exception;
	
	public void sample() throws Exception;
	
	public void insert(Map parameterMap) throws Exception;

}

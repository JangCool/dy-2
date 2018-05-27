package kr.co.pionnet.dy.dao;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.pionnet.dy.datasource.DataSourceGeneration;

@Repository
public class SampleDaoTrx {
	
	@Autowired
	DataSourceGeneration dataSourceGeneration;

	public int insert(Map parameterMap) throws Exception {
		parameterMap.put("subject", "sadfdsafdsaf");
		parameterMap.put("content", "cccccccccccc");
		parameterMap.put("user_id", 111111111);
		parameterMap.put("user_name", "jangcool");
		
		return dataSourceGeneration.getSqlSession("test").insert("kr.co.pionnet.dy.dao.SampleDaoTrx.insert",parameterMap);
	}

	
	
}

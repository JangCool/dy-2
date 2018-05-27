package kr.co.pionnet.dy.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.pionnet.dy.datasource.DataSourceGeneration;

@Repository
public class SampleDao {
	
	@Autowired
	DataSourceGeneration dataSourceGeneration;

	public List<Map<String, ?>> getDual() {
		return dataSourceGeneration.getSqlSession("test").selectList("kr.co.pionnet.dy.dao.SampleDao.getDual");
	}

	
	
}

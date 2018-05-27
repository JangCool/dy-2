package kr.co.pionnet.dy.service.sample;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pionnet.dy.TransactionalDynamic;
import kr.co.pionnet.dy.dao.SampleDao;
import kr.co.pionnet.dy.dao.SampleDaoTrx;

@Service
public class SampleServiceImpl implements SampleService{

	@Autowired
	SampleDao sampleDao;
	
	@Autowired
	SampleDaoTrx sampleDaoTrx;
	
	@Autowired
	SampleServiceImpl sampleServiceImpl;
	
	@Override
	@TransactionalDynamic
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false,rollbackFor=Exception.class)
	public List<Map<String,?>> getDual() throws Exception {
		sampleServiceImpl.insert(new HashMap());
		sampleServiceImpl.insert(new HashMap());
		sampleServiceImpl.insert(new HashMap());
//		if(true) {
//			throw new SQLException();
//		}
		return sampleDao.getDual();
	}

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void sample()  throws Exception{
//		if(true) {
//			throw new SQLException();
//		}
//		
	}

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void insert(Map parameterMap) throws Exception {
		sampleDaoTrx.insert(parameterMap);

		
	}

}

package kr.co.pionnet.dy.datasource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;


@Component
public class DataSourceGeneration {
	
	
	private Map<String, SqlSessionTemplate> sqlSessions = Collections.synchronizedMap(new HashMap<>());
	
	private DataSourceEnv dataSourceEnv;
	
	private ApplicationContext applicationContext;
	
	public DataSourceGeneration() {}
	
	public DataSourceGeneration(DataSourceEnv dataSourceEnv,ApplicationContext applicationContext) {
		this.dataSourceEnv = dataSourceEnv;
		this.applicationContext = applicationContext;
	}
	
	
	public DataSourceEnv getDataSourceEnv() {
		return dataSourceEnv;
	}

	public void setDataSourceEnv(DataSourceEnv dataSourceEnv) {
		this.dataSourceEnv = dataSourceEnv;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * datasource를 생성한다.
	 * @param evnh
	 * @return
	 */
	public DataSource getDataSource(DataSourceEnv dataSourceEnv) {
		
		if(this.dataSourceEnv == null) {
			this.dataSourceEnv = dataSourceEnv;
		}

		DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
	    dataSourceBuilder.url(this.dataSourceEnv.getUrl());
	    dataSourceBuilder.driverClassName(this.dataSourceEnv.getDriverClassName());
	    dataSourceBuilder.username(this.dataSourceEnv.getUserName());
	    dataSourceBuilder.password(this.dataSourceEnv.getPassword());
	    return dataSourceBuilder.build();   
	}
	
	public SqlSessionFactory sqlSessionFactory(DataSource firstDataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean(); 
		sqlSessionFactoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
		sqlSessionFactoryBean.setDataSource(firstDataSource); 
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mappers/oracle/**/*.xml"));
		
		return sqlSessionFactoryBean.getObject(); 
	} 
	
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory firstSqlSessionFactory) throws Exception {
		SqlSessionTemplate sqlSessionTemplate = null;
		
		String name = dataSourceEnv.getName();
		
		if(!sqlSessions.containsKey(name)) {
			sqlSessionTemplate = new SqlSessionTemplate(firstSqlSessionFactory); 
			sqlSessions.put(name, sqlSessionTemplate);
		} else {
			sqlSessionTemplate = sqlSessions.get(name);
		}
		
		return sqlSessionTemplate; 
	}
	
	public DataSourceTransactionManager transactionManager(DataSource dataSource) throws Exception {
		
		DefaultListableBeanFactory factory = (DefaultListableBeanFactory) ((ConfigurableApplicationContext) applicationContext).getBeanFactory();

		// 멤버 필드 값 셋팅
		MutablePropertyValues propertyValues = new MutablePropertyValues();
		propertyValues.addPropertyValue("dataSource", dataSource);
		// bean 생성
		GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setBeanClass(DataSourceTransactionManager.class);
		beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
		beanDefinition.setPropertyValues(propertyValues);	
		factory.registerBeanDefinition(dataSourceEnv.getName().concat("TransactionManager"), beanDefinition);
		
		return null;
	}
	
	
	public SqlSessionTemplate getSqlSession(String name) {
		return sqlSessions.get(name);		
	}

	
	
	
}

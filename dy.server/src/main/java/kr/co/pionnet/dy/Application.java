package kr.co.pionnet.dy;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import kr.co.pionnet.dy.datasource.DataSourceEnv;
import kr.co.pionnet.dy.datasource.DataSourceGeneration;

@SpringBootApplication (exclude = {DataSourceAutoConfiguration.class})
@EnableTransactionManagement(proxyTargetClass = true)
public class Application {


	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private DataSourceGeneration dataSourceGeneration;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	
	@Bean
	InitializingBean setDataSource() {
		return () -> {
			DataSourceEnv dse = new DataSourceEnv();
			dse.setUrl("jdbc:mysql://218.239.127.229/test");
			dse.setDriverClassName("org.mariadb.jdbc.Driver");
			dse.setUserName("root");
			dse.setPassword("jangcool");
			dse.setName("test");
			
			dataSourceGeneration.setApplicationContext(context);
			DataSource dataSource = dataSourceGeneration.getDataSource(dse);
			
			dataSourceGeneration.sqlSessionTemplate(dataSourceGeneration.sqlSessionFactory(dataSource));
			dataSourceGeneration.transactionManager(dataSource);
			
			DataSourceEnv dse2 = new DataSourceEnv();
			dse.setUrl("jdbc:mysql://218.239.127.229/test");
			dse.setDriverClassName("org.mariadb.jdbc.Driver");
			dse.setUserName("root");
			dse.setPassword("jangcool");
			dse.setName("test2");
			
			dataSourceGeneration.setApplicationContext(context);
			DataSource dataSource2 = dataSourceGeneration.getDataSource(dse2);
			
			dataSourceGeneration.sqlSessionTemplate(dataSourceGeneration.sqlSessionFactory(dataSource2));
			dataSourceGeneration.transactionManager(dataSource2);
		};
	}
	
	@Bean
	public ApplicationRunner run() {
		return args -> {
			for (String arg : args.getNonOptionArgs()) {
				System.out.println("ApplicationRunner line " +arg);

			}
			
//			System.out.println("ApplicationRunner line ");
		};
	}
	
	@Bean
	public CommandLineRunner runner() {
		return args -> {
			for (String arg : args) {
				System.out.println("CommandLineRunner line " +arg);

			}
		};
	}
}

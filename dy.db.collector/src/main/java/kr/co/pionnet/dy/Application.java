package kr.co.pionnet.dy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication (exclude = {DataSourceAutoConfiguration.class})
@EnableTransactionManagement(proxyTargetClass = true)
public class Application {


	@Autowired
	private ApplicationContext context;

	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
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

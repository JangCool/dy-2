package kr.co.pionnet.dy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import kr.co.pionnet.dy.common.Config;
import kr.co.pionnet.dy.net.NetServerFactory;
import kr.co.pionnet.dy.type.NetType;

@SpringBootApplication (exclude = {DataSourceAutoConfiguration.class})
@EnableTransactionManagement(proxyTargetClass = true)
public class Application {


	@Autowired
	private ApplicationContext context;

	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		
		System.out.println("################");
	}

	
	/***
	 * 어플리케이션이 시작 할 때 초기 화 및 기본 설정 값을 이곳에서 설정한다.
	 * java 실행 할 시 넘겨주는 option 값들도 이곳에서 처리 할 수 있다. ex) --name -name args
	 * @return
	 */
	@Bean
	public ApplicationRunner run() {
		
		String ip = Config.getString("net.server.udp.ip");
		int port = Config.getInt("net.server.udp.port");
		
		
		return args -> {
			

			
			
			for (String arg : args.getNonOptionArgs()) {
				System.out.println("ApplicationRunner line " +arg);
			}

			
			NetServerFactory.getInstance().createServer(NetType.UDP, ip, port).start();

		};
	}

}

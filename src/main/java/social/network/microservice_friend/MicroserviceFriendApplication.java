package social.network.microservice_friend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MicroserviceFriendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceFriendApplication.class, args);
	}

}

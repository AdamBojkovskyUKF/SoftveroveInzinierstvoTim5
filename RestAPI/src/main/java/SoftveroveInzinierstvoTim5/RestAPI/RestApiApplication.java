package SoftveroveInzinierstvoTim5.RestAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableAutoConfiguration
@EnableSpringConfigured
@ComponentScan
public class RestApiApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(RestApiApplication.class, args);
	}

}

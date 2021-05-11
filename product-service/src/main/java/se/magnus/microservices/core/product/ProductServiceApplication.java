package se.magnus.microservices.core.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("se.magnus")
public class ProductServiceApplication {
	private final static Logger log = LoggerFactory.getLogger(ProductServiceApplication.class);

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(ProductServiceApplication.class, args);
		String mongoHost = ctx.getEnvironment().getProperty("spring.data.mongodb.host");
		String mongoPort = ctx.getEnvironment().getProperty("spring.data.mongodb.port");
		log.info("Connected to MongoDB: {}:{}", mongoHost, mongoPort);
	}

}

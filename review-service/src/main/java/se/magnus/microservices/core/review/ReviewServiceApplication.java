package se.magnus.microservices.core.review;

import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
@ComponentScan("se.magnus")
public class ReviewServiceApplication {
	private final Logger log = LoggerFactory.getLogger(ReviewServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ReviewServiceApplication.class, args);
	}

	private final Integer connectionPoolSize;

	@Autowired
	public ReviewServiceApplication(@Value("${spring.datasource.maximum-pool-size:10}") Integer connectionPoolSize){
		this.connectionPoolSize = connectionPoolSize;
	}

	@Bean
	Scheduler jdbcScheduler(){
		log.debug("Creating JDBC scheduler thread pool with {} threads", connectionPoolSize);
		return Schedulers.fromExecutor(Executors.newFixedThreadPool(connectionPoolSize));
	}

}

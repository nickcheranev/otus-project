package ru.ncheranev.otus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
@Slf4j
public class RssExtractorApplication {
	public static void main(String[] args) {
		SpringApplication.run(RssExtractorApplication.class, args);
	}
}

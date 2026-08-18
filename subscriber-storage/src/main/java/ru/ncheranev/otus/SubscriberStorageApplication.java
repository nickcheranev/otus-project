package ru.ncheranev.otus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.ncheranev.otus.service.PopulateService;

@SpringBootApplication
@EnableJpaRepositories
@Slf4j
public class SubscriberStorageApplication {
    public static void main(String[] args) {
        var context = SpringApplication.run(SubscriberStorageApplication.class, args);
        var populateService = context.getBean(PopulateService.class);
        populateService.populate();
    }
}
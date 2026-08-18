package ru.ncheranev.otus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableAspectJAutoProxy
@Slf4j
public class ArticleStorageApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArticleStorageApplication.class, args);
    }
}

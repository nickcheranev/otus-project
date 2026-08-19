package ru.ncheranev.otus.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    /**
     * Настройка Retryer
     *
     * @return Retryer
     */
    @Bean
    public Retryer retryer() {
        // Параметры: period (начальная задержка), maxPeriod (максимальная задержка), maxAttempts (кол-во попыток)
        return new Retryer.Default(1000L, 5000L, 3);
    }
}
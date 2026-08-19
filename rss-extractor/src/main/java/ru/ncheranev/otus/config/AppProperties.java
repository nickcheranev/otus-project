package ru.ncheranev.otus.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "app")
@Data
@Accessors(chain = true)
public class AppProperties {

    /**
     * Данные поставщика RSS
     */
    private Source source;
    /**
     * Топики событий Kafka
     */
    private Map<String, Event> events;

    @Data
    @Accessors(chain = true)
    public static class Source {
        /**
         * URL источника
         */
        private String url;
        /**
         * Ключ
         */
        private String key;
    }

    @Data
    @Accessors(chain = true)
    public static class Event {
        private String topic;
    }
}

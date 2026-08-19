package ru.ncheranev.otus.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
@Accessors(chain = true)
public class AppProperties {

    private Vk vk;

    @Data
    public static class Vk {
        /**
         * URL метода создания комментария
         */
        private String createCommentMethodUrl;
        /**
         * Ключ доступа сообщества
         */
        private String accessToken;
        /**
         * Идентификатор сообщества
         */
        private String ownerId;
        /**
         * Идентификатор поста
         */
        private String postId;
    }
}

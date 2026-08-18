package ru.ncheranev.otus.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Агрегат Статья/Подписчик
 */
@Data
@Accessors(chain = true)
public class CommunicatorDto {
    private ArticleDto article;
    private SubscriberDto subscriber;
}

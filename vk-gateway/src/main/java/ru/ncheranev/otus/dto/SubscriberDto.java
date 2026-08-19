package ru.ncheranev.otus.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * Данные подписчика, включая категории
 */
@Data
@Accessors(chain = true)
public class SubscriberDto {
    /**
     * Идентификатор подписчика
     */
    private Long id;
    /**
     * Имя подписчика
     */
    private String name;
    /**
     * Список категорий
     */
    private Set<String> categories;
    /**
     * Email подписчика
     */
    private String email;
}

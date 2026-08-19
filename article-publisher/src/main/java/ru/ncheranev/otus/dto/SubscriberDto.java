package ru.ncheranev.otus.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * Подписчик
 */
@Data
@Accessors(chain = true)
public class SubscriberDto {
    private Long id;
    private String name;
    private Set<String> categories;
}

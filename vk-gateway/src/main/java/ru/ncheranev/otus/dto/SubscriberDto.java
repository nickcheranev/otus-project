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
    private Long id;
    private String name;
    private Set<String> categories;
    private String email;
}

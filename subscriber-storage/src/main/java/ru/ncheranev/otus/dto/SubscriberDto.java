package ru.ncheranev.otus.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Данные подписчика
 */
@Data
@Accessors(chain = true)
public class SubscriberDto {
    private Long id;
    private String name;
    private List<String> categories;
    private String email;
}

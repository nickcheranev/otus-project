package ru.ncheranev.otus.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Статья
 */
@Data
@Accessors(chain = true)
public class ArticleDto {
    /**
     * Идентификатор
     */
    private Long id;
    /**
     * Ссылка
     */
    private String uri;
    /**
     * Заголовок
     */
    private String title;
    /**
     * Ссылка
     */
    private String link;
    /**
     * Описание
     */
    private String description;
    /**
     * Дата публикации
     */
    private LocalDateTime publishDate;
    /**
     * Автор
     */
    private String author;
    /**
     * Категории
     */
    private Set<String> categories;
    /**
     * Источник
     */
    private String source;
    /**
     * Состояние
     */
    private String state;
}

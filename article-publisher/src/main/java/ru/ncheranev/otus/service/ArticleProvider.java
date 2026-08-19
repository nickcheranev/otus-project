package ru.ncheranev.otus.service;

import ru.ncheranev.otus.dto.ArticleDto;

import java.util.Optional;

/**
 * Поставщик данных о статьях
 */
public interface ArticleProvider {
    /**
     * Получить статью по идентификатору
     *
     * @param id идентификатор
     * @return статья, если найдена
     */
    Optional<ArticleDto> getById(Long id);
}

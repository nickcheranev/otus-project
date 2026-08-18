package ru.ncheranev.otus.service;

import ru.ncheranev.otus.dto.ArticleDto;

import java.util.Optional;

/**
 * Поставщик данных о статьях
 */
public interface ArticleProvider {
    Optional<ArticleDto> getById(Long id);
}

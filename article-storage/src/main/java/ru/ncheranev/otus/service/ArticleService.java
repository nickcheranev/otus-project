package ru.ncheranev.otus.service;

import ru.ncheranev.otus.dto.ArticleDto;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы со статьями
 */
public interface ArticleService {
    ArticleDto create(ArticleDto articleDto);

    ArticleDto categorize(Long articleId, List<String> categoryNames);

    void publishToKafka(Long articleId);

    List<ArticleDto> findAll();

    Optional<ArticleDto> findById(Long id);

    Optional<ArticleDto> findByUri(String uri);
}

package ru.ncheranev.otus.service;

import ru.ncheranev.otus.dto.ArticleDto;

/**
 * Сервис создания статьи (фасад)
 */
public interface CreateArticleService {
    ArticleDto createArticle(ArticleDto articleDto);
}

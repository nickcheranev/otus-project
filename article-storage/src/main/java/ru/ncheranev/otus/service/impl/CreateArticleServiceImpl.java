package ru.ncheranev.otus.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ncheranev.otus.dto.ArticleDto;
import ru.ncheranev.otus.service.ArticleService;
import ru.ncheranev.otus.service.CreateArticleService;

/**
 * Реализация сервиса создания статьи (фасад)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreateArticleServiceImpl implements CreateArticleService {
    private final ArticleService articleService;

    @Override
    @Transactional
    public ArticleDto createArticle(ArticleDto articleDto) {
        var createdArticle = articleService.create(articleDto);
        log.info("Создана статья с id: {}", createdArticle.getId());

        var categorizedArticle = articleService.categorize(createdArticle.getId(), articleDto.getCategories());
        log.info("Статье присвоены категории: {}", categorizedArticle);

        articleService.publishToKafka(categorizedArticle.getId());
        log.info("Статья с id={} опубликована в Kafka", categorizedArticle.getId());

        return categorizedArticle;
    }
}

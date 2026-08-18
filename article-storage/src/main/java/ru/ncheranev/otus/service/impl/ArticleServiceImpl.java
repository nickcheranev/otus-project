package ru.ncheranev.otus.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ncheranev.otus.config.AppProperties;
import ru.ncheranev.otus.dto.ArticleDto;
import ru.ncheranev.otus.jpa.repository.ArticleRepository;
import ru.ncheranev.otus.jpa.repository.CategoryRepository;
import ru.ncheranev.otus.service.ArticleService;
import ru.ncheranev.otus.service.CategoryService;
import ru.ncheranev.otus.util.MapperUtil;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Сервис для работы со статьями (реализация)
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final KafkaTemplate<String, Long> kafkaTemplate;
    private final CategoryService categoryService;
    private final AppProperties appProperties;

    @Override
    @Transactional
    public ArticleDto create(ArticleDto articleDto) {
        if (articleRepository.findByUri(articleDto.getUri()).isEmpty()) {
            var newArticle = MapperUtil.toEntity(articleDto);
            var saved = articleRepository.save(newArticle);
            log.info("Сохранена статья: {}", saved);

            return MapperUtil.toDto(saved);
        } else {
            log.warn("Статья уже имеется: {}", articleDto.getUri());
            return articleDto;
        }
    }

    @Override
    @Transactional
    public ArticleDto categorize(Long articleId, List<String> categoryNames) {
        // Создать отсутствующие категории
        categoryService.createAbsentByNames(categoryNames);
        var categories = categoryRepository.findAllByNameIn(categoryNames);

        var article = articleRepository.findById(articleId).orElseThrow();
        article.setCategories(categories);
        var categorizedArticle = articleRepository.save(article);

        return MapperUtil.toDto(categorizedArticle);
    }

    @Override
    @Transactional
    public void publishToKafka(Long articleId) {
        var topic = appProperties.getEvents().get("article-created").getTopic();
        ProducerRecord<String, Long> message = new ProducerRecord<>(topic, articleId.toString(), articleId);
        kafkaTemplate.send(message);
    }

    @Override
    @Transactional(readOnly=true)
    public List<ArticleDto> findAll() {
        return articleRepository.findAll().stream().map(MapperUtil::toDto).toList();
    }

    @Override
    @Transactional(readOnly=true)
    public Optional<ArticleDto> findById(Long id) {
        var found = articleRepository.findById(id);
        if (found.isPresent()) {
            return Optional.of(MapperUtil.toDto(found.get()));
        } else {
            throw new NoSuchElementException("Не найдена статья с идентификатором " + id);
        }
    }

    @Override
    @Transactional(readOnly=true)
    public Optional<ArticleDto> findByUri(String uri) {
        return articleRepository.findByUri(uri).map(MapperUtil::toDto);
    }
}

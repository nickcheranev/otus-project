package ru.ncheranev.otus.service;

/**
 * Публикация статей
 */
public interface PublisherService {
    /**
     * Опубликовать статью с идентификатором
     *
     * @param articleId идентификатор статьи
     */
    void publish(Long articleId);
}

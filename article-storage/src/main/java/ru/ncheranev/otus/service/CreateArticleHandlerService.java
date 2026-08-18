package ru.ncheranev.otus.service;

import org.springframework.messaging.Message;

/**
 * Сервис обработчика создание статьи
 */
public interface CreateArticleHandlerService {
    void createArticleHandle(Message<String> message);
}

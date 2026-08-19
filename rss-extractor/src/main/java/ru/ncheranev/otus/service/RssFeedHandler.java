package ru.ncheranev.otus.service;

import org.springframework.messaging.Message;

/**
 * Обработчик события 'Получена запись RSS feed'
 */
public interface RssFeedHandler {
    void handle(Message<?> syndMessage);
}

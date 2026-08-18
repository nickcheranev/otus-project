package ru.ncheranev.otus.service;

import org.springframework.messaging.Message;

public interface RssFeedHandler {
    void handle(Message<?> syndMessage);
}

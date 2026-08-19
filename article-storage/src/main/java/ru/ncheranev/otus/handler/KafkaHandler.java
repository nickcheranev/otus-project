package ru.ncheranev.otus.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.ncheranev.otus.service.CreateArticleHandlerService;

/**
 * Обработчик событий Kafka
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaHandler {
    private final CreateArticleHandlerService createArticleHandlerService;

    /**
     * Обработать событие 'Получен RSS feed'
     *
     * @param message сообщение
     */
    @KafkaListener(id = "otus-group", topics = "${app.events.rss-feed-received.topic}")
    public void listen(Message<String> message) {
        log.debug("Получено из Kafka: {}", message);
        createArticleHandlerService.createArticleHandle(message);
    }
}

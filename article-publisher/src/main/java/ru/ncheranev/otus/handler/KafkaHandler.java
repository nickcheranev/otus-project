package ru.ncheranev.otus.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.ncheranev.otus.service.PublisherService;

/**
 * Обработчик событий Kafka
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaHandler {
    private final PublisherService publisherService;

    /**
     * Событие 'Статья создана/категоризирована'
     *
     * @param message Сообщение
     */
    @KafkaListener(id = "otus-group", topics = "${app.events.article-created.topic}")
    public void listen(Message<Long> message) {
        log.debug("Получено из Kafka: {}", message);

        publisherService.publish(message.getPayload());
    }
}

package ru.ncheranev.otus.service.impl;

import com.rometools.rome.feed.synd.SyndEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import ru.ncheranev.otus.config.AppProperties;
import ru.ncheranev.otus.jpa.repository.EntryRepository;
import ru.ncheranev.otus.service.RssFeedHandler;
import ru.ncheranev.otus.util.MapperUtil;

import java.time.LocalDateTime;


/**
 * Обработчик события 'Получена запись RSS feed'
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RssFeedHandlerImpl implements RssFeedHandler {
    private final EntryRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AppProperties appProperties;

    /**
     * Обработать событие 'RSS feed получен'
     *
     * @param syndMessage сообщение с RSS feed
     */
    @Override
    public void handle(Message<?> syndMessage) {
        var syndEntry = (SyndEntry) syndMessage.getPayload();

        log.debug("Получено из Kafka {}", syndEntry.getUri());

        var entry = MapperUtil.toDto(syndEntry);

        var saved = repository.save(MapperUtil.toEntity(entry));

        log.debug("Сохранено в БД: {}", saved);

        var topic = appProperties.getEvents().get("rss-feed-received").getTopic();
        ProducerRecord<String, String> message = new ProducerRecord<>(topic,
                LocalDateTime.now().toString(), entry.getUri());

        kafkaTemplate.send(message);

        log.info("Отправлено в Kafka: {} ", message);
    }
}

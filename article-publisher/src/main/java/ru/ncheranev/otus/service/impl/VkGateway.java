package ru.ncheranev.otus.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.ncheranev.otus.config.AppProperties;
import ru.ncheranev.otus.dto.CommunicatorDto;
import ru.ncheranev.otus.service.CommunicatorService;

/**
 * Публикация в ВКонтакте (реализация)
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class VkGateway implements CommunicatorService {
    private final KafkaTemplate<String, CommunicatorDto> kafkaTemplate;
    private final AppProperties appProperties;

    @Override
    public void sendMessage(CommunicatorDto dto) {
        var topic = appProperties.getEvents().get("vk-gateway").getTopic();
        ProducerRecord<String, CommunicatorDto> message = new ProducerRecord<>(topic, dto.getArticle().getLink(), dto);
        kafkaTemplate.send(message);
    }
}

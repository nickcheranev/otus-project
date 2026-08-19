package ru.ncheranev.otus.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.ncheranev.otus.dto.CommunicatorDto;
import ru.ncheranev.otus.service.MessageProducer;
import ru.ncheranev.otus.util.MapperUtil;

/**
 * Обработчик событий Kafka
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaHandler {
    private final MessageProducer messageProducer;

    /**
     * Обработчик сообщения 'Сообщение готово к отправке'
     *
     * @param request сообщение
     */
    @KafkaListener(id = "otus-group", topics = "${app.events.vk-gateway.topic}")
    public void listen(Message<CommunicatorDto> request) {
        log.debug("Получено из Kafka: {}", request);
        var comment = MapperUtil.toCreateComment(request.getPayload());
        messageProducer.wallCreateComment_v1(comment);
    }
}

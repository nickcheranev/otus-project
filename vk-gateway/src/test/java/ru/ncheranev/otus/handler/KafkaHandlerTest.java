package ru.ncheranev.otus.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import ru.ncheranev.otus.dto.ArticleDto;
import ru.ncheranev.otus.dto.CommunicatorDto;
import ru.ncheranev.otus.service.MessageProducer;
import ru.ncheranev.otus.util.MapperUtil;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("класс KafkaHandler")
class KafkaHandlerTest {
    @Mock
    private MessageProducer messageProducer;
    @InjectMocks
    private KafkaHandler sut;

    @Test
    @DisplayName("метод listen должен обработать сообщение из Kafka")
    void listen() {
        // given
        Message<CommunicatorDto> request = new GenericMessage<>(new CommunicatorDto()
                .setArticle(new ArticleDto()));
        var comment = MapperUtil.toCreateComment(request.getPayload());

        // when
        sut.listen(request);

        // then
        verify(messageProducer).wallCreateComment_v1(comment);

    }
}
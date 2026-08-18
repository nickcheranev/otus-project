package ru.ncheranev.otus.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import ru.ncheranev.otus.service.PublisherService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование KafkaHandlerTest обработчика сообщений Kafka")
class KafkaHandlerTest {
    @Mock
    private PublisherService publisherService;
    @InjectMocks
    private KafkaHandler sut;

    @Test
    @DisplayName("должен обработать сообщение из Kafka")
    void shouldHandleKafkaMessage() {
        // when
        Message<Long> message = new GenericMessage<>(1L);
        assertThatNoException().isThrownBy(() -> sut.listen(message));

        // then
        verify(publisherService).publish(anyLong());
    }
}

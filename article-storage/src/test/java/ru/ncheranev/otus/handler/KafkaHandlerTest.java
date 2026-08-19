package ru.ncheranev.otus.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ncheranev.otus.service.CreateArticleHandlerService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование KafkaHandlerTest (обработчик сообщений Кафка)")
class KafkaHandlerTest {
    @Mock
    private CreateArticleHandlerService createArticleHandlerService;
    @InjectMocks
    private KafkaHandler sut;

    @Test
    @DisplayName("должен вызвать метод createArticleHandle() у createArticleHandlerService")
    void shouldHandleKafkaMessage() {
        // when
        assertThatNoException().isThrownBy(() -> sut.listen(any()));

        // then
        verify(createArticleHandlerService).createArticleHandle(any());
    }
}

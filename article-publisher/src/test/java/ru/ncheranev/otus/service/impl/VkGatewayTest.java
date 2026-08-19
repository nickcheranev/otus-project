package ru.ncheranev.otus.service.impl;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import ru.ncheranev.otus.config.AppProperties;
import ru.ncheranev.otus.dto.ArticleDto;
import ru.ncheranev.otus.dto.CommunicatorDto;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VkGatewayTest {
    private static final AppProperties appProperties = new AppProperties()
            .setEvents(Map.of("vk-gateway", new AppProperties.Event().setTopic("vk-gateway-topic")));
    @Mock
    private KafkaTemplate<String, CommunicatorDto> kafkaTemplate;
    @InjectMocks
    private VkGateway sut;
    @Captor
    private ArgumentCaptor<ProducerRecord<String, CommunicatorDto>> captor;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(sut, "appProperties", appProperties);
    }

    @Test
    @DisplayName("Должен отправить сообщение в vk-gateway")
    void shouldSendDtoToKafka() {
        // given
        var dto = new CommunicatorDto()
                .setArticle(new ArticleDto().setLink("link"));

        // when & then
        sut.sendMessage(dto);
        verify(kafkaTemplate).send(captor.capture());
        ProducerRecord<String, CommunicatorDto> captorValue = captor.getValue();
        assertThat(captorValue.topic()).isEqualTo("vk-gateway-topic");
        assertThat(captorValue.value()).isEqualTo(dto);
    }
}

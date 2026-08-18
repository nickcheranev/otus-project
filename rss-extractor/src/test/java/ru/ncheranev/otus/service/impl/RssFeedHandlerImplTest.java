package ru.ncheranev.otus.service.impl;

import com.rometools.rome.feed.synd.SyndEntry;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.util.ReflectionTestUtils;
import ru.ncheranev.otus.config.AppProperties;
import ru.ncheranev.otus.jpa.domain.Entry;
import ru.ncheranev.otus.jpa.repository.EntryRepository;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RssFeedHandlerImpl обработчик RSS feed")
class RssFeedHandlerImplTest {
    @Mock
    private EntryRepository repository;
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;
    @InjectMocks
    private RssFeedHandlerImpl sut;

    @Test
    @DisplayName("должен обработать получение RSS feed")
    void shouldHandleReceiveRssFeed() {
        // given
        var appProperties = new AppProperties().setEvents(Map
                .of("rss-feed-received", new AppProperties.Event().setTopic("rss-feed-received-topic")));
        ReflectionTestUtils.setField(sut, "appProperties", appProperties);

        var entry = new Entry();
        when(repository.save(any())).thenReturn(entry);

        var mockSyndEntry = mock(SyndEntry.class);
        var message = new GenericMessage<>(mockSyndEntry);

        // when

        sut.handle(message);

        // then
        verify(kafkaTemplate).send(any(ProducerRecord.class));
    }
}
package ru.ncheranev.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ncheranev.otus.dto.ArticleDto;
import ru.ncheranev.otus.dto.CommunicatorDto;
import ru.ncheranev.otus.dto.SubscriberDto;
import ru.ncheranev.otus.service.ArticleProvider;
import ru.ncheranev.otus.service.CommunicatorService;
import ru.ncheranev.otus.service.SubscriberProvider;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublisherServiceImplTest {
    @Mock
    private SubscriberProvider subscriberProvider;
    @Mock
    private ArticleProvider articleProvider;
    @Mock
    private CommunicatorService communicator;
    @InjectMocks
    private PublisherServiceImpl sut;

    @Captor
    private ArgumentCaptor<CommunicatorDto> communicatorArgumentCaptor;

    @Test
    @DisplayName("Успешное выполнение publish с публикацией одного сообщения")
    void publish_whenNoException_thenSuccess() {
        // given
        var categories = Set.of("category1", "category2");
        var article = new ArticleDto().setId(1L).setCategories(categories);
        when(articleProvider.getById(1L)).thenReturn(Optional.of(article));
        var subscribers = List.of(new SubscriberDto().setId(1L).setCategories(categories));
        when(subscriberProvider.getByCategory(categories)).thenReturn(subscribers);

        // when & then
        sut.publish(1L);

        verify(communicator).sendMessage(communicatorArgumentCaptor.capture());
        var communicatorArgumentCaptorValue = communicatorArgumentCaptor.getValue();
        assertThat(communicatorArgumentCaptorValue.getArticle()).isEqualTo(article);
        assertThat(communicatorArgumentCaptorValue.getSubscriber()).isEqualTo(subscribers.get(0));
    }

    @Test
    @DisplayName("Успешное выполнение publish без публикации сообщений при их отсутствии")
    void publish_whenArticleAbsent_thenSkipCommunicatorSendMessage() {
        // given
        when(articleProvider.getById(1L)).thenReturn(Optional.empty());

        // when & then
        sut.publish(1L);

        verifyNoInteractions(communicator);
    }

    @Test
    @DisplayName("Успешное выполнение publish без публикации сообщений при отсутствии подписчиков")
    void publish_whenNoSubscriber_thenSkipCommunicatorSendMessage() {
        // given
        var categories = Set.of("category1", "category2");
        var article = new ArticleDto().setId(1L).setCategories(categories);
        when(articleProvider.getById(1L)).thenReturn(Optional.of(article));
        var subscribers = List.<SubscriberDto>of();
        when(subscriberProvider.getByCategory(categories)).thenReturn(subscribers);

        // when & then
        sut.publish(1L);

        verifyNoInteractions(communicator);
    }
}

package ru.ncheranev.otus.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import ru.ncheranev.otus.dto.ArticleDto;
import ru.ncheranev.otus.dto.EntryDto;
import ru.ncheranev.otus.service.ArticleService;
import ru.ncheranev.otus.service.CreateArticleService;
import ru.ncheranev.otus.service.EntryProvider;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Класс CreateArticleHandlerServiceImpl (обработчик события создания статьи)")
@ExtendWith(MockitoExtension.class)
class CreateArticleHandlerServiceImplTest {

    @Mock
    private CreateArticleService createArticleService;
    @Mock
    private ArticleService articleService;
    @Mock
    private EntryProvider entryProvider;

    @InjectMocks
    private CreateArticleHandlerServiceImpl sut;

    @Test
    @DisplayName("должен создать статью, если статьи с таким uri еще нет")
    void createArticleHandle_articleNotFound_createsArticle() {
        // given
        String uri = "https://example.com/article/1";
        Message<String> message = MessageBuilder.withPayload(uri)
                .build();

        EntryDto entryDto = new EntryDto();
        entryDto.setUri(uri);

        when(articleService.findByUri(uri)).thenReturn(Optional.empty());
        when(entryProvider.getEntry(eq(uri), any())).thenReturn(entryDto);
        when(createArticleService.createArticle(any(ArticleDto.class))).thenReturn(new ArticleDto());

        // when
        sut.createArticleHandle(message);

        // then
        verify(entryProvider).getEntry(eq(uri), any());
        verify(articleService).findByUri(uri);
        verify(createArticleService).createArticle(any(ArticleDto.class));
    }

    @Test
    @DisplayName("должен пропустить создание статьи, если статья уже существует")
    void createArticleHandle_articleAlreadyExists_skipsCreation() {
        // given
        String uri = "https://example.com/article/1";
        Message<String> message = MessageBuilder.withPayload(uri)
                .build();

        when(articleService.findByUri(uri)).thenReturn(Optional.of(new ArticleDto()));

        // when
        sut.createArticleHandle(message);

        // then
        verify(entryProvider).getEntry(eq(uri), any());
        verify(articleService).findByUri(uri);
        verify(createArticleService, never()).createArticle(any());
    }

    @Test
    @DisplayName("должен выдать сообщение в лог и не выдавать исключение при ошибке создания статьи")
    void createArticleHandle_createArticleThrowsException_handlesGracefully() {
        // given
        String uri = "https://example.com/article/1";
        Message<String> message = MessageBuilder.withPayload(uri)
                .build();

        EntryDto entryDto = new EntryDto();
        entryDto.setUri(uri);

        when(articleService.findByUri(uri)).thenReturn(Optional.empty());
        when(entryProvider.getEntry(eq(uri), any())).thenReturn(entryDto);
        when(createArticleService.createArticle(any(ArticleDto.class)))
                .thenThrow(new RuntimeException("DB error"));

        // when / then - should not throw, exception is caught inside
        Assertions.assertDoesNotThrow(() -> sut.createArticleHandle(message));
    }
}

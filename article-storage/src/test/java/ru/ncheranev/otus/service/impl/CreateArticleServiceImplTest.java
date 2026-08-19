package ru.ncheranev.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ncheranev.otus.dto.ArticleDto;
import ru.ncheranev.otus.service.ArticleService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateArticleServiceImplTest {
    private static final LocalDateTime PUBLISH_DATE_TIME = LocalDateTime.now();

    @Mock
    private ArticleService articleService;
    @InjectMocks
    private CreateArticleServiceImpl sut;

    @Test
    @DisplayName("должен создать статью, присвоить ей категории и отправить в кафку")
    void shouldCreateArticleAndCategorizeAndSendToKafka() {
        // given
        var article = new ArticleDto()
                .setUri("uri")
                .setTitle("title")
                .setLink("link")
                .setDescription("description")
                .setPublishDate(PUBLISH_DATE_TIME)
                .setAuthor("author")
                .setCategories(List.of("category1"))
                .setSource("source");
        when(articleService.create(article)).thenReturn(article.setId(1L));
        when(articleService.categorize(article.getId(), article.getCategories())).thenReturn(article);

        // when
        var actual = sut.createArticle(article);

        // then
        assertThat(actual).isNotNull();
        verify(articleService).publishToKafka(article.getId());
    }
}

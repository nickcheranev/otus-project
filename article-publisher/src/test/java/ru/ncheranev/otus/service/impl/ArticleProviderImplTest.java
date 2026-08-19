package ru.ncheranev.otus.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestOperations;
import ru.ncheranev.otus.config.AppProperties;
import ru.ncheranev.otus.dto.ArticleDto;
import ru.ncheranev.otus.oauth.OAuth2TokenService;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleProviderImplTest {
    private final AppProperties appProperties = new AppProperties()
            .setResources(Map.of("article-storage", new AppProperties.Resource().setUrl("url")));
    @Mock
    private RestOperations restOperations;
    @Mock
    private OAuth2TokenService tokenService;
    @InjectMocks
    private ArticleProviderImpl sut;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(sut, "appProperties", appProperties);
    }

    @Test
    @DisplayName("getById должен вернуть статью по идентификатору")
    void getById_shouldReturnArticle() {
        // given
        when(tokenService.getAccessToken()).thenReturn("access-token");
        var expected = Optional.of(new ArticleDto());
        when(restOperations.exchange(eq("url/article/1"), eq(HttpMethod.GET), any(), eq(ArticleDto.class), eq(1L)))
                .thenReturn(ResponseEntity.of(expected));

        // when
        var actual = sut.getById(1L);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("При исключении должен вернуть Optional.empty()")
    void getById_whenException_shouldReturnEmpty() {
        // given
        when(tokenService.getAccessToken()).thenThrow(RuntimeException.class);

        // when
        var actual = sut.getById(1L);

        // then
        assertThat(actual).isEmpty();
    }
}

package ru.ncheranev.otus.service.impl;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestOperations;
import ru.ncheranev.otus.config.AppProperties;
import ru.ncheranev.otus.dto.SubscriberDto;
import ru.ncheranev.otus.oauth.OAuth2TokenService;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriberProviderImplTest {
    private final AppProperties appProperties = new AppProperties()
            .setResources(Map.of("subscriber-storage", new AppProperties.Resource().setUrl("url")));
    @Mock
    private RestOperations restOperations;
    @Mock
    private OAuth2TokenService tokenService;
    @InjectMocks
    private SubscriberProviderImpl sut;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(sut, "appProperties", appProperties);
    }

    @Test
    @DisplayName("getById должен вернуть подписчика по категориям")
    void getById_shouldReturnArticle() {
        // given
        when(tokenService.getAccessToken()).thenReturn("access-token");
        var expectedSubscribers = List.of(new SubscriberDto());
        ResponseEntity<List<SubscriberDto>> responseEntity = new ResponseEntity<>(expectedSubscribers, HttpStatus.OK);
        when(restOperations.exchange(eq("url/subscriber"), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        // when
        var actual = sut.getByCategory(Set.of("category1"));

        // then
        assertThat(actual).isEqualTo(responseEntity.getBody());
    }

    @Test
    @DisplayName("При исключении должен вернуть List.empty()")
    void getById_whenException_shouldReturnEmpty() {
        // given
        when(tokenService.getAccessToken()).thenThrow(RuntimeException.class);

        // when
        var actual = sut.getByCategory(Set.of());

        // then
        assertThat(actual).asInstanceOf(InstanceOfAssertFactories.LIST).isEmpty();
    }
}

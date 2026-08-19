package ru.ncheranev.otus.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestOperations;
import ru.ncheranev.otus.config.AppProperties;
import ru.ncheranev.otus.dto.EntryDto;
import ru.ncheranev.otus.dto.UriInfo;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntryProviderImplTest {
    @Mock
    private RestOperations restOperations;
    @InjectMocks
    private EntryProviderImpl sut;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(sut, "appProperties", new AppProperties()
                .setResources(Map.of("rss-extractor", new AppProperties.Resource().setUrl("url"))));
    }

    @Test
    void getEntry_success() {
        // given
        String uri = "https://example.com/article/1";
        var expectedEntry = new EntryDto();
        expectedEntry.setTitle("Test Article");
        expectedEntry.setUri(uri);

        when(restOperations.exchange(
                eq("url/entry"),
                eq(HttpMethod.GET),
                argThat(entity -> {
                    assertNotNull(entity.getBody());
                    assertThat(((UriInfo) entity.getBody()).getUri()).isEqualTo(uri);
                    return true;
                }),
                eq(EntryDto.class)
        )).thenReturn(ResponseEntity.ok(expectedEntry));

        // when
        var actual = sut.getEntry(uri, new HttpHeaders());

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getTitle()).isEqualTo("Test Article");
        assertThat(actual.getUri()).isEqualTo(uri);
    }

    @Test
    void getEntry_runtimeException_throwsNoSuchElementException() {
        // given
        var uri = "https://example.com/article/1";

        when(restOperations.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(EntryDto.class)
        )).thenThrow(new RuntimeException("Internal server error"));

        // when / then
        assertThatThrownBy(() -> sut.getEntry(uri, new HttpHeaders())).isInstanceOf(NoSuchElementException.class);
    }
}

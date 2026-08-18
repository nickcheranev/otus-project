package ru.ncheranev.otus.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import ru.ncheranev.otus.config.AppProperties;
import ru.ncheranev.otus.dto.CreateCommentRequest;
import ru.ncheranev.otus.dto.CreateCommentResponse;
import ru.ncheranev.otus.service.VkApiClient;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("класс MessageProducerImpl")
class MessageProducerImplTest {
    @Mock
    private RestOperations restOperations;
    @Mock
    private VkApiClient vkApiClient;
    @InjectMocks
    private MessageProducerImpl sut;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(sut, "appProperties", new AppProperties()
                .setVk(new AppProperties.Vk()
                        .setAccessToken("accessToken")
                        .setCreateCommentMethodUrl("http://localhost/")
                        .setOwnerId("ownerId")
                        .setPostId("postId")));
    }

    @Captor
    private ArgumentCaptor<HttpEntity<MultiValueMap<String, String>>> httpEntityArgumentCaptor;

    @Test
    @DisplayName("должен отправить сообщение createComment в api VK используя restTemplate")
    void shouldSendWallCreateComment_V1() {
        // given
        var request = new CreateCommentRequest().setMessage("comment");
        var successResponse = ResponseEntity.ok(new CreateCommentResponse()
                .setResponse(new CreateCommentResponse.Response().setComment_id("1")));
        when(restOperations.postForEntity(anyString(), any(), eq(CreateCommentResponse.class)))
                .thenReturn(successResponse);

        // when
        var actual = sut.wallCreateComment_v1(request);

        // then
        assertThat(actual).isTrue();
        verify(restOperations).postForEntity(eq("http://localhost/"),
                httpEntityArgumentCaptor.capture(), eq(CreateCommentResponse.class));
        var value = httpEntityArgumentCaptor.getValue();
        var actualBody = value.getBody();
        var actualHeader = value.getHeaders();
        Assertions.assertNotNull(actualBody);
        assertThat(actualBody.get("owner_id").get(0)).isEqualTo("ownerId");
        assertThat(actualBody.get("post_id").get(0)).isEqualTo("postId");
        assertThat(actualBody.get("message").get(0)).isEqualTo("comment");
        assertThat(requireNonNull(actualHeader.get("Authorization")).get(0)).isEqualTo("Bearer accessToken");
    }

    @Captor
    private ArgumentCaptor<LinkedMultiValueMap<String, String>> mapCaptor;

    @Test
    @DisplayName("должен отправить сообщение createComment в api VK используя feign client")
    void shouldSendWallCreateComment_V2() {
        // given
        var request = new CreateCommentRequest().setMessage("comment");
        ResponseEntity<CreateCommentResponse> successResponse = ResponseEntity.ok(new CreateCommentResponse()
                .setResponse(new CreateCommentResponse.Response().setComment_id("1")));
        when(vkApiClient.wallCreateComment(anyString(), any())).thenReturn(successResponse);

        // when
        var actual = sut.wallCreateComment_v2(request);

        // then
        assertThat(actual).isTrue();
        verify(vkApiClient).wallCreateComment(anyString(), mapCaptor.capture());
        var value = mapCaptor.getValue();
        assertThat(requireNonNull(value.get("owner_id")).get(0)).isEqualTo("ownerId");
        assertThat(requireNonNull(value.get("post_id")).get(0)).isEqualTo("postId");
        assertThat(requireNonNull(value.get("message")).get(0)).isEqualTo("comment");
    }

    @Nested
    @DisplayName("тестирование метода isCommentCreated (ответ API VK) - отрицательные сценарии")
    class IsCommentCreatedTest {
        private static Stream<Arguments> negativeScenarios_methodSource() {
            return Stream.of(
                    Arguments.of(ResponseEntity.status(500).body(null)),
                    Arguments.of(ResponseEntity.status(200).body(null)),
                    Arguments.of(ResponseEntity.status(200).body(new CreateCommentResponse())),
                    Arguments.of(ResponseEntity.status(200).body(new CreateCommentResponse()
                            .setResponse(new CreateCommentResponse.Response())))
            );
        }

        @Test
        @DisplayName("response == null")
        void isCommentCreated_whenNullArgument_thenReturnFalse() {
            assertThat(sut.isCommentCreated(null)).isFalse();
        }

        @ParameterizedTest
        @MethodSource("negativeScenarios_methodSource")
        @DisplayName("отрицательный сценарий")
        void isCommentCreated_falseVariants(ResponseEntity<CreateCommentResponse> response) {
            assertThat(sut.isCommentCreated(response)).isFalse();
        }
    }

}

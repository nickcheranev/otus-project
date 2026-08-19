package ru.ncheranev.otus.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import ru.ncheranev.otus.config.AppProperties;
import ru.ncheranev.otus.dto.CreateCommentRequest;
import ru.ncheranev.otus.dto.CreateCommentResponse;
import ru.ncheranev.otus.service.MessageProducer;
import ru.ncheranev.otus.service.VkApiClient;

import static java.util.Objects.isNull;

/**
 * Отправка сообщений в VK API
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageProducerImpl implements MessageProducer {
    private final AppProperties appProperties;
    private final RestOperations restOperations;
    private final VkApiClient vkApiClient;

    /**
     * Создание комментария на стене сообщества (RestOperations)
     *
     * @param request данные запроса
     * @return id созданного комментария
     */
    @Override
    public boolean wallCreateComment_v1(CreateCommentRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(appProperties.getVk().getAccessToken());

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("owner_id", appProperties.getVk().getOwnerId());
        body.add("post_id", appProperties.getVk().getPostId());
        body.add("message", request.getMessage());

        var entity = new HttpEntity<>(body, headers);
        log.info("Создание комментария: {}", body);
        var response = restOperations.postForEntity(appProperties.getVk().getCreateCommentMethodUrl(),
                entity, CreateCommentResponse.class);
        return isCommentCreated(response);
    }

    /**
     * Создание комментария на стене сообщества (Feign, Retry)
     *
     * @param request данные запроса
     * @return id созданного комментария
     */
    @Override
    public boolean wallCreateComment_v2(CreateCommentRequest request) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("owner_id", appProperties.getVk().getOwnerId());
        body.add("post_id", appProperties.getVk().getPostId());
        body.add("message", request.getMessage());

        log.info("Создание комментария (feign): {}", body);

        var response = vkApiClient.wallCreateComment("Bearer " + appProperties.getVk().getAccessToken(), body);
        return isCommentCreated(response);
    }

    boolean isCommentCreated(ResponseEntity<CreateCommentResponse> response) {
        if (isNull(response) ||
                response.getStatusCode() != HttpStatus.OK ||
                isNull(response.getBody()) ||
                isNull(response.getBody().getResponse()) ||
                StringUtils.isBlank(response.getBody().getResponse().getComment_id())) {
            log.info("Комментарий не создан, ответ: {}", response);
            return false;
        } else {
            log.info("Комментарий создан с результатом, ответ: {}", response);
            return true;
        }
    }
}

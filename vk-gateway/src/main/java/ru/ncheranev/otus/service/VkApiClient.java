package ru.ncheranev.otus.service;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.ncheranev.otus.dto.CreateCommentResponse;

@FeignClient(
        name = "vk-api-client",
        url = "${app.vk.create-comment-method-url}",
        configuration = FeignClientsConfiguration.class // настройка Retry
)
@Headers("Content-Type: application/x-www-form-urlencoded")
public interface VkApiClient {
    @GetMapping
    ResponseEntity<CreateCommentResponse> wallCreateComment(@RequestHeader("Authorization") String token,
                                                            MultiValueMap<String, String> body);
}

package ru.ncheranev.otus.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import ru.ncheranev.otus.config.AppProperties;
import ru.ncheranev.otus.dto.CategoryInfo;
import ru.ncheranev.otus.dto.SubscriberDto;
import ru.ncheranev.otus.oauth.OAuth2TokenService;
import ru.ncheranev.otus.service.SubscriberProvider;

import java.util.List;
import java.util.Set;

/**
 * Поставщик данных о подписчиках (реализация)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriberProviderImpl implements SubscriberProvider {
    private final RestOperations restOperations;
    private final AppProperties appProperties;
    private final OAuth2TokenService tokenService;

    /**
     * Получить подписчиков, подписанных на категории
     *
     * @param categories список категорий
     * @return список подписчиков
     */
    @Override
    public List<SubscriberDto> getByCategory(Set<String> categories) {
        try {
            var token = tokenService.getAccessToken();
            var responseType = new ParameterizedTypeReference<List<SubscriberDto>>() {
            };
            var headers = new HttpHeaders();
            headers.setBearerAuth(token);
            var httpEntity = new HttpEntity<>(new CategoryInfo().setNames(categories), headers);
            var url = appProperties.getResources().get("subscriber-storage").getUrl() + "/subscriber";
            return restOperations.exchange(url,
                    HttpMethod.GET,
                    httpEntity,
                    responseType).getBody();
        } catch (RuntimeException re) {
            log.error(re.getMessage(), re);
            return List.of();
        }
    }
}

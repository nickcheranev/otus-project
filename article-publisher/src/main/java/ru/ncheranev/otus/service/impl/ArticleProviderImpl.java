package ru.ncheranev.otus.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import ru.ncheranev.otus.config.AppProperties;
import ru.ncheranev.otus.dto.ArticleDto;
import ru.ncheranev.otus.oauth.OAuth2TokenService;
import ru.ncheranev.otus.service.ArticleProvider;

import java.util.Optional;

/**
 * Реализация сервиса, предоставляющего информацию о статьях
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleProviderImpl implements ArticleProvider {
    private final RestOperations restOperations;
    private final AppProperties appProperties;
    private final OAuth2TokenService tokenService;

    /**
     * Получить данные статьи
     *
     * @param id id статьи
     * @return данные статьи
     */
    @Override
    public Optional<ArticleDto> getById(Long id) {
        try {
            var token = tokenService.getAccessToken();
            var url = appProperties.getResources().get("article-storage").getUrl() + "/article/" + id;
            var headers = new HttpHeaders();
            headers.setBearerAuth(token);
            var httpEntity = new HttpEntity<>(headers);
            var result = Optional.ofNullable(restOperations.exchange(url, HttpMethod.GET, httpEntity, ArticleDto.class, id)
                    .getBody());
            log.info("Получена статья: {}", result);
            return result;
        } catch (RuntimeException re) {
            log.error(re.getMessage(), re);
            return Optional.empty();
        }
    }
}

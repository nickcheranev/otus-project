package ru.ncheranev.otus.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;

import java.time.LocalDateTime;

/**
 * Сервис для получения OAuth2 токена
 */
@Slf4j
@Service
public class OAuth2TokenServiceImpl implements OAuth2TokenService {

    private final RestOperations restOperations;
    private final String tokenUri;
    private final String clientId;
    private final String clientSecret;
    private final String scope;

    private String cachedAccessToken;
    private LocalDateTime tokenExpiryTime;

    public OAuth2TokenServiceImpl(
            RestOperations restOperations,
            OAuth2ClientProperties properties) {

        this.restOperations = restOperations;
        var registration = properties.getRegistration().get("article-publisher");
        this.tokenUri = properties.getProvider().get("auth-server").getTokenUri();
        this.clientId = registration.getClientId();
        this.clientSecret = registration.getClientSecret();
        this.scope = String.join(" ", registration.getScope());
    }

    /**
     * Получить / запросить токен
     *
     * @return токен
     */
    @Override
    public synchronized String getAccessToken() {
        if (cachedAccessToken != null && tokenExpiryTime != null
                && LocalDateTime.now().isBefore(tokenExpiryTime)) {
            log.debug("Используем кешированный токен");
            return cachedAccessToken;
        }

        log.info("Получение нового токена от Authorization Server");
        TokenResponse tokenResponse = requestNewToken();

        this.cachedAccessToken = tokenResponse.getAccessToken();
        this.tokenExpiryTime = LocalDateTime.now()
                .plusSeconds(tokenResponse.getExpiresIn() - 60);

        log.info("Токен получен. Действует до: {}", tokenExpiryTime);
        return cachedAccessToken;
    }

    private TokenResponse requestNewToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("scope", scope);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        try {
            ResponseEntity<TokenResponse> response = restOperations.postForEntity(
                    tokenUri,
                    request,
                    TokenResponse.class
            );

            if (response.getBody() == null || response.getBody().getAccessToken() == null) {
                throw new RuntimeException("Не удалось получить токен");
            }

            return response.getBody();
        } catch (Exception e) {
            log.error("Ошибка при получении токена: {}", e.getMessage());
            throw new RuntimeException("Не удалось получить токен доступа", e);
        }
    }
}

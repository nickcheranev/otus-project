package ru.ncheranev.otus.oauth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestOperations;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuth2TokenServiceImplTest {

    private OAuth2TokenServiceImpl tokenService;
    private static final OAuth2ClientProperties oauth2ClientProperties;
    static {
        oauth2ClientProperties = new OAuth2ClientProperties();
        var registration =  new OAuth2ClientProperties.Registration();
        registration.setClientId("clientId");
        registration.setClientSecret("clientSecret");
        registration.setScope(Set.of("scope"));
        oauth2ClientProperties.getRegistration().put("article-storage", registration);
        OAuth2ClientProperties.Provider provider = new OAuth2ClientProperties.Provider();
        provider.setTokenUri("tokenUri");
        oauth2ClientProperties.getProvider().put("auth-server", provider);
    }
    @Mock
    private RestOperations restOperations;

    @BeforeEach
    void setUp() {
        tokenService = new OAuth2TokenServiceImpl(restOperations, oauth2ClientProperties);
    }

    @Test
    void getAccessToken_usesCachedToken() {
        // given
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken("cached-token");
        tokenResponse.setExpiresIn(3600);

        when(restOperations.postForEntity(
                anyString(),
                any(HttpEntity.class),
                eq(TokenResponse.class)
        )).thenReturn(ResponseEntity.ok(tokenResponse));

        // when
        String firstToken = tokenService.getAccessToken();
        String secondToken = tokenService.getAccessToken();

        // then
        assertEquals(firstToken, secondToken);
    }

    @Test
    void getAccessToken_resourceAccessException_throwsRuntimeException() {
        // given
        when(restOperations.postForEntity(
                anyString(),
                any(HttpEntity.class),
                eq(TokenResponse.class)
        )).thenThrow(new ResourceAccessException("Connection refused"));

        // when / then
        assertThrows(RuntimeException.class, () -> tokenService.getAccessToken());
    }

    @Test
    void getAccessToken_nullResponse_throwsRuntimeException() {
        // given
        when(restOperations.postForEntity(
                anyString(),
                any(HttpEntity.class),
                eq(TokenResponse.class)
        )).thenReturn(ResponseEntity.ok(new TokenResponse()));

        // when / then
        assertThrows(RuntimeException.class, () -> tokenService.getAccessToken());
    }
}

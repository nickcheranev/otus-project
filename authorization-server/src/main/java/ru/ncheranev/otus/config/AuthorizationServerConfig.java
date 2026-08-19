package ru.ncheranev.otus.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;

@Configuration
public class AuthorizationServerConfig {

    /**
     * Репозиторий зарегистрированных клиентов
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        // Создаем клиента article-storage
        RegisteredClient articleStorage = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId("article-storage")
                .clientSecret("{noop}article-storage-secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scopes(scopes -> scopes.add("internal.read"))
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED) // JWT
                        .accessTokenTimeToLive(Duration.ofHours(1)) // 1 час
                        .refreshTokenTimeToLive(Duration.ofDays(1)) // 24 часа
                        .reuseRefreshTokens(true)
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false) // Не требуется согласие пользователя
                        .requireProofKey(false)
                        .build())
                .build();

        // Создаем клиента article-publisher
        RegisteredClient articlePublisher = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId("article-publisher")
                .clientSecret("{noop}article-publisher-secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scopes(scopes -> scopes.add("internal.read"))
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED) // JWT
                        .accessTokenTimeToLive(Duration.ofHours(1)) // 1 час
                        .refreshTokenTimeToLive(Duration.ofDays(1)) // 24 часа
                        .reuseRefreshTokens(true)
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false) // Не требуется согласие пользователя
                        .requireProofKey(false)
                        .build())
                .build();

        return new InMemoryRegisteredClientRepository(articleStorage, articlePublisher);
    }

    /**
     * Источник JWK для подписи JWT
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * Генерация RSA ключей
     */
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    /**
     * Декодер JWT
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * Кастомизатор токенов - добавление дополнительных claims
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                context.getClaims().claims(claims -> {
                    claims.put("service_name", context.getRegisteredClient().getClientId());
                    claims.put("token_type", "machine-to-machine");
                    claims.put("issued_at", System.currentTimeMillis());

                    // Добавляем информацию о клиенте
                    claims.put("client_id", context.getRegisteredClient().getClientId());

                    // Можно добавить кастомные атрибуты
                    claims.put("custom_attr", "custom_value");
                });
            }
        };
    }
}

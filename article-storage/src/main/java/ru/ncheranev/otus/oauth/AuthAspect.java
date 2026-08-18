package ru.ncheranev.otus.oauth;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * Аспект добавления заголовка авторизации {@link Auth}
 */
@Component
@Aspect
@RequiredArgsConstructor
public class AuthAspect {
    private final OAuth2TokenService tokenService;

    @Around("@annotation(auth) && args(uri, headers)")
    public Object addAuthHeaderMethod(ProceedingJoinPoint pjp, Auth auth, String uri, HttpHeaders headers) throws Throwable {
        var token = tokenService.getAccessToken();
        headers.setBearerAuth(token);
        return pjp.proceed(pjp.getArgs());
    }
}

package ru.ncheranev.otus.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import ru.ncheranev.otus.config.AppProperties;
import ru.ncheranev.otus.dto.EntryDto;
import ru.ncheranev.otus.dto.UriInfo;
import ru.ncheranev.otus.oauth.Auth;
import ru.ncheranev.otus.service.EntryProvider;

import java.util.NoSuchElementException;

/**
 * Поставщик записей RSS feed
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EntryProviderImpl implements EntryProvider {
    private final RestOperations restOperations;
    private final AppProperties appProperties;

    @Override
    @Auth
    public EntryDto getEntry(String uri, HttpHeaders headers) throws NoSuchElementException {
        try {
            var extractorUrl = appProperties.getResources().get("rss-extractor").getUrl();
            var httpEntity = new HttpEntity<>(new UriInfo().setUri(uri), headers);
            var result = restOperations.exchange(extractorUrl + "/entry",
                            HttpMethod.GET,
                            httpEntity,
                            EntryDto.class)
                    .getBody();
            log.info("Получен entry: {}", result);
            return result;
        } catch (RuntimeException re) {
            log.error(re.getMessage(), re);
            throw new NoSuchElementException(re);
        }
    }
}

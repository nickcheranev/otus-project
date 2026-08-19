package ru.ncheranev.otus.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.feed.dsl.Feed;
import ru.ncheranev.otus.service.RssFeedHandler;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @see <a href="https://docs.spring.io/spring-integration/reference/feed.html">Integrate Feeds</a>
 */
@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final AppProperties appProperties;

    /**
     * Бин канала входящих данных RSS ленты
     *
     * @param handler обработчик входящих данных
     * @return бин канала
     * @throws MalformedURLException исключение некорректного URL
     */
    @Bean
    public IntegrationFlow poolFeedFlow(RssFeedHandler handler) throws MalformedURLException {
        return IntegrationFlow.from(Feed.inboundAdapter(new URL(appProperties.getSource().getUrl()), appProperties.getSource().getKey()),
                        e -> e.poller(p -> p.fixedDelay(100)))
                .handle(handler::handle)
                .get();
    }

}

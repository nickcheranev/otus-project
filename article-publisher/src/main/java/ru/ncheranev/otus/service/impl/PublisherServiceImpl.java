package ru.ncheranev.otus.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.ncheranev.otus.dto.CommunicatorDto;
import ru.ncheranev.otus.dto.SubscriberDto;
import ru.ncheranev.otus.service.ArticleProvider;
import ru.ncheranev.otus.service.CommunicatorService;
import ru.ncheranev.otus.service.SubscriberProvider;
import ru.ncheranev.otus.service.PublisherService;

/**
 * Сервис (реализация) отправляет сообщение о готовности статьи к публикации в Кафку
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {
    private final SubscriberProvider subscriberProvider;
    private final ArticleProvider articleProvider;
    private final CommunicatorService communicator;

    /**
     * Отправить сообщение о готовности статьи к публикации в Кафку
     *
     * @param articleId идентификатор статьи
     */
    @Override
    public void publish(Long articleId) {
        var optArticle = articleProvider.getById(articleId);
        if (optArticle.isPresent()) {
            var article = optArticle.get();
            log.debug("Статья с id={} готова к публикации", article.getId());
            var categories = article.getCategories();
            var subscribers = subscriberProvider.getByCategory(categories);
            if (!CollectionUtils.isEmpty(subscribers)) {
                log.info("Найдены подписчики: {} с категориями: {}", subscribers.stream().map(SubscriberDto::getName).toList(), categories);
                subscribers.forEach(subscriber -> {
                    communicator.sendMessage(
                            new CommunicatorDto()
                                    .setArticle(article)
                                    .setSubscriber(subscriber));
                    log.info("Отправлено сообщение: {} подписчику: {}", article.getId(), subscriber.getName());
                });
            } else {
                log.warn("Нет подписчиков для категорий: {}", categories);
            }
        }
    }
}

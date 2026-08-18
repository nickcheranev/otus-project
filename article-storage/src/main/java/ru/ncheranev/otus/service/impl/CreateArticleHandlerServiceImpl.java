package ru.ncheranev.otus.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import ru.ncheranev.otus.service.ArticleService;
import ru.ncheranev.otus.service.CreateArticleHandlerService;
import ru.ncheranev.otus.service.CreateArticleService;
import ru.ncheranev.otus.service.EntryProvider;
import ru.ncheranev.otus.util.MapperUtil;

/**
 * Сервис обработчика создание статьи (реализация)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreateArticleHandlerServiceImpl implements CreateArticleHandlerService {

    private final CreateArticleService createArticleService;
    private final ArticleService articleService;
    private final EntryProvider entryProvider;

    @Override
    public void createArticleHandle(Message<String> message) {
        var uri = message.getPayload();
        try {
            var entry = entryProvider.getEntry(uri, new HttpHeaders());
            if (articleService.findByUri(uri).isEmpty()) {
                var createdArticle = createArticleService.createArticle(MapperUtil.toArticle(entry));
                log.debug("Создана статья: {}", createdArticle);
            }
        } catch (RuntimeException re) {
            log.error("Ошибка создания статьи", re);
        }
    }
}

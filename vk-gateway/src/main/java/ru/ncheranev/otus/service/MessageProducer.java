package ru.ncheranev.otus.service;

import ru.ncheranev.otus.dto.CreateCommentRequest;

public interface MessageProducer {
    /**
     * Создание комментария на стене сообщества (RestOperations)
     *
     * @param request данные запроса
     * @return id созданного комментария
     */
    boolean wallCreateComment_v1(CreateCommentRequest request);

    /**
     * Создание комментария на стене сообщества (Feign)
     *
     * @param request данные запроса
     * @return id созданного комментария
     */
    boolean wallCreateComment_v2(CreateCommentRequest request);
}

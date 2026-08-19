package ru.ncheranev.otus.service;

import ru.ncheranev.otus.dto.SubscriberDto;

import java.util.List;
import java.util.Set;

/**
 * Данные о подписчиках
 */
public interface SubscriberProvider {
    /**
     * Получить список подписчиков по списку категорий
     *
     * @param categories список категорий
     * @return список подписчиков
     */
    List<SubscriberDto> getByCategory(Set<String> categories);
}

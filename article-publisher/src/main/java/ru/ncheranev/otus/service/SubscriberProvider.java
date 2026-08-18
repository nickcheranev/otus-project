package ru.ncheranev.otus.service;

import ru.ncheranev.otus.dto.SubscriberDto;

import java.util.List;
import java.util.Set;

/**
 * Данные о подписчиках
 */
public interface SubscriberProvider {
    List<SubscriberDto> getByCategory(Set<String> categories);
}

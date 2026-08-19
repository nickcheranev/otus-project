package ru.ncheranev.otus.service;

import ru.ncheranev.otus.dto.SubscriberDto;

import java.util.List;

public interface SubscriberService {
    List<SubscriberDto> findByCategories(List<String> categories);
}

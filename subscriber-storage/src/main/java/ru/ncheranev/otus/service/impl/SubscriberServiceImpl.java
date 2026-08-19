package ru.ncheranev.otus.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ncheranev.otus.dto.SubscriberDto;
import ru.ncheranev.otus.jpa.repository.SubscriberRepository;
import ru.ncheranev.otus.service.SubscriberService;
import ru.ncheranev.otus.util.MapperUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с подписчиками
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriberServiceImpl implements SubscriberService {
    private final SubscriberRepository subscriberRepository;

    /**
     * Получить список подписчиков по списку категорий
     *
     * @param categories категории
     * @return список подписчиков
     */
    @Override
    @Transactional
    public List<SubscriberDto> findByCategories(List<String> categories) {
        var subscribers = subscriberRepository.findByCategoriesNameIn(categories)
                .stream().map(MapperUtil::toDto).collect(Collectors.toList());
        log.info("Подписчики: {}, найдены для категорий: {}",
                subscribers.stream().map(SubscriberDto::getName).toList(),
                categories);
        return subscribers;
    }
}

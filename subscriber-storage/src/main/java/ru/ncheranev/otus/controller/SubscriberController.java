package ru.ncheranev.otus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ncheranev.otus.dto.CategoryInfo;
import ru.ncheranev.otus.dto.SubscriberDto;
import ru.ncheranev.otus.jpa.domain.Subscriber;
import ru.ncheranev.otus.service.SubscriberService;

import java.util.List;

/**
 * REST контроллер для работы с подписчиками {@link Subscriber}
 */
@RestController
@RequestMapping("/subscriber")
@RequiredArgsConstructor
@Slf4j
public class SubscriberController {
    private final SubscriberService subscriberService;

    /**
     * Получить подписчиков по списку категорий
     *
     * @param categoryInfo список категорий
     * @return подписчики
     */
    @GetMapping
    public List<SubscriberDto> getAllByCategoryNames(@RequestBody CategoryInfo categoryInfo) {
        return subscriberService.findByCategories(categoryInfo.getNames());
    }
}

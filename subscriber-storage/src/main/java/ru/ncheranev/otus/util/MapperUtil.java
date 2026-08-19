package ru.ncheranev.otus.util;

import lombok.experimental.UtilityClass;
import ru.ncheranev.otus.dto.SubscriberDto;
import ru.ncheranev.otus.jpa.domain.Category;
import ru.ncheranev.otus.jpa.domain.Subscriber;

@UtilityClass
public class MapperUtil {
    public static SubscriberDto toDto(Subscriber entity) {
        return new SubscriberDto()
                .setId(entity.getId())
                .setName(entity.getName())
                .setCategories(entity.getCategories().stream().map(Category::getName).toList())
                .setEmail(entity.getEmail());
    }
}

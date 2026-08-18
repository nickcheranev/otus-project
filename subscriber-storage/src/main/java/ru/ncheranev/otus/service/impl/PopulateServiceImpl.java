package ru.ncheranev.otus.service.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ncheranev.otus.jpa.domain.Category;
import ru.ncheranev.otus.jpa.domain.Subscriber;
import ru.ncheranev.otus.service.PopulateService;

import java.util.Arrays;
import java.util.List;

/**
 * Заполнение БД тестовыми данными подписчик/категории
 */
@Service
@RequiredArgsConstructor
public class PopulateServiceImpl implements PopulateService {
    private final EntityManager em;

    @Override
    @Transactional
    public void populate() {
        var categories = createCategories("ai", "ии", "искусственный интеллект", "микросервисы", "spring", "java", "diy", "deepseek");

        categories.forEach(em::persist);

        var subscriber1 = new Subscriber().setName("subscriber").setCategories(categories);

        em.persist(subscriber1);
    }

    private List<Category> createCategories(String... names) {
        return Arrays.stream(names).map(name -> new Category().setName(name)).toList();
    }
}

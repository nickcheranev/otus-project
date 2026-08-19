package ru.ncheranev.otus.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.ncheranev.otus.jpa.domain.Category;
import ru.ncheranev.otus.jpa.repository.CategoryRepository;
import ru.ncheranev.otus.service.CategoryService;

import java.util.List;

/**
 * Сервис для работы с категориями (реализация)
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    /**
     * Создать отсутствующие категории, по списку наименований
     *
     * @param names Список наименований категорий
     */
    @Override
    @Transactional
    public void createAbsentByNames(List<String> names) {
        if (CollectionUtils.isEmpty(names)) {
            return;
        }
        var present = categoryRepository.findAllByNameIn(names);
        var absents = names.stream()
                .filter(name -> !present.stream().map(Category::getName).toList().contains(name))
                .toList();
        categoryRepository.saveAll(absents.stream().map(name -> new Category().setName(name)).toList());
    }
}

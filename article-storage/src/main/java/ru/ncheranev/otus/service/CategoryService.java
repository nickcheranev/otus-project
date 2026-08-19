package ru.ncheranev.otus.service;

import java.util.List;

/**
 * Сервис для работы с категориями
 */
public interface CategoryService {
    void createAbsentByNames(List<String> names);
}

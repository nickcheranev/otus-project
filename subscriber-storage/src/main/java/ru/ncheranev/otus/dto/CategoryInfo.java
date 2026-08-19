package ru.ncheranev.otus.dto;

import lombok.Data;

import java.util.List;

/**
 * Список категорий
 */
@Data
public class CategoryInfo {
    private List<String> names;
}

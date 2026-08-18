package ru.ncheranev.otus.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * Категория
 */
@Data
@Accessors(chain = true)
public class CategoryInfo {
    private Set<String> names;
}

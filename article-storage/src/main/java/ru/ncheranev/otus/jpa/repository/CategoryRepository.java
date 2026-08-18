package ru.ncheranev.otus.jpa.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.ncheranev.otus.jpa.domain.Category;

import java.util.List;

/**
 * JPA репозиторий категорий {@link Category}
 */
@Repository
public interface CategoryRepository extends ListCrudRepository<Category, String> {
    List<Category> findAllByNameIn(List<String> categories);
}

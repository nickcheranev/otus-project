package ru.ncheranev.otus.jpa.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.ncheranev.otus.jpa.domain.Article;

import java.util.Optional;

/**
 * JPA репозиторий статей {@link Article}
 */
@Repository
public interface ArticleRepository extends ListCrudRepository<Article, Long> {
    Optional<Article> findByUri(String uri);
}

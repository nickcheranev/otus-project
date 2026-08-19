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
    /**
     * Поиск статьи по URI
     *
     * @param uri URI статьи
     * @return статья, если найдена
     */
    Optional<Article> findByUri(String uri);
}

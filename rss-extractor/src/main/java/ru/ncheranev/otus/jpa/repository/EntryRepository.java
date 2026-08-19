package ru.ncheranev.otus.jpa.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.ncheranev.otus.jpa.domain.Entry;

import java.util.Optional;

/**
 * Репозиторий для работы с записями RSS feed
 */
@Repository
public interface EntryRepository extends ListCrudRepository<Entry, String> {

    /**
     * Получить запись по URI
     *
     * @param uri URI записи
     * @return запись
     */
    Optional<Entry> findByUri(String uri);
}

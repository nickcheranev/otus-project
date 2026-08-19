package ru.ncheranev.otus.jpa.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.ncheranev.otus.jpa.domain.Subscriber;

import java.util.List;

/**
 * Репозиторий для работы с подписчиками
 */
@Repository
public interface SubscriberRepository extends ListCrudRepository<Subscriber, Long> {
    /**
     * Получить список подписчиков по списку категорий
     *
     * @param names список категорий
     * @return список подписчиков
     */
    List<Subscriber> findByCategoriesNameIn(List<String> names);
}

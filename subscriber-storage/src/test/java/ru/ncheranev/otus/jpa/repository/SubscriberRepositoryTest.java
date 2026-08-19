package ru.ncheranev.otus.jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.ncheranev.otus.jpa.domain.Category;
import ru.ncheranev.otus.jpa.domain.Subscriber;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class SubscriberRepositoryTest {
    @Autowired
    private SubscriberRepository sut;
    @PersistenceContext
    private EntityManager em;

    @Test
    @Transactional
    void findByCategoriesNameInTest() {
        var category1 = new Category().setName("category1");
        var category2 = new Category().setName("category2");
        var category3 = new Category().setName("category3");
        em.persist(category1);
        em.persist(category2);
        em.persist(category3);

        var subscriber1 = new Subscriber()
                .setName("name1")
                .setCategories(List.of(category1));

        var subscriber2 = new Subscriber()
                .setName("name2")
                .setCategories(List.of(category2));

        var subscriber3 = new Subscriber()
                .setName("name3")
                .setCategories(List.of(category3));

        sut.save(subscriber1);
        sut.save(subscriber2);
        sut.save(subscriber3);

        var actual = sut.findByCategoriesNameIn(List.of("category1", "category2"));
        assertThat(actual).isNotNull();
    }
}
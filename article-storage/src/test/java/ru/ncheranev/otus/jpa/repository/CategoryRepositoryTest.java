package ru.ncheranev.otus.jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.ncheranev.otus.jpa.domain.Category;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository sut;
    @PersistenceContext
    private EntityManager em;

    @Test
    @Transactional
    void findAllByNameInTest() {

        em.persist(new Category().setName("name1"));
        em.persist(new Category().setName("name2"));
        em.persist(new Category().setName("name3"));

        var actual = sut.findAllByNameIn(List.of("name1", "name2"));
        assertThat(actual).isNotNull();
    }
}

package ru.ncheranev.otus.jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.ncheranev.otus.jpa.domain.Article;
import ru.ncheranev.otus.jpa.domain.Category;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ArticleRepositoryTest {
    LocalDateTime PUBLISH_DATE_TIME = LocalDateTime.of(2026, 3, 26, 10, 11);
    @Autowired
    private ArticleRepository sut;
    @PersistenceContext
    private EntityManager em;

    @Test
    @Transactional
    void findByUriTest() {
        var category = new Category()
                .setName("category");
        em.persist(category);

        var article = new Article()
                .setUri("uri")
                .setTitle("title")
                .setLink("link")
                .setDescription("description")
                .setPublishDate(PUBLISH_DATE_TIME)
                .setAuthor("author")
                .setCategories(List.of(category))
                .setSource("source");

        var saved = em.merge(article);
        assertThat(saved).isNotNull();

        var actual = sut.findByUri("uri");
        assertThat(actual.orElseThrow()).isEqualTo(saved);
    }
}

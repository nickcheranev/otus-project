package ru.ncheranev.otus.jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.ncheranev.otus.jpa.domain.Entry;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@DisplayName("EntryRepository")
class EntryRepositoryTest {
    @Autowired
    private EntryRepository entryRepository;
    @PersistenceContext
    private EntityManager em;

    @Test
    @Transactional
    @DisplayName("должен найти запись по uri")
    void findByUri() {
        // given
        var entry = new Entry()
                .setUri("uri");
        em.persist(entry);

        // when
        var actual = entryRepository.findByUri("uri");

        // then
        assertThat(actual).isEqualTo(Optional.of(entry));
    }
}

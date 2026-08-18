package ru.ncheranev.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ncheranev.otus.jpa.domain.Entry;
import ru.ncheranev.otus.jpa.repository.EntryRepository;
import ru.ncheranev.otus.model.EntryDto;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты сервиса для работы с записями RSS ленты EntryServiceImpl")
class EntryServiceImplTest {
    @Mock
    private EntryRepository entryRepository;
    @InjectMocks
    private EntryServiceImpl sut;

    @Test
    @DisplayName("должен найти запись по uri")
    void shouldFindEntryByUri() {
        // given
        Entry entry = new Entry().setUri("uri");
        when(entryRepository.findByUri("uri")).thenReturn(Optional.of(entry));

        // when
        var actual = sut.findByUri("uri");

        // then
        assertThat(actual).isPresent().get().isEqualTo(new EntryDto().setUri("uri"));

    }
}

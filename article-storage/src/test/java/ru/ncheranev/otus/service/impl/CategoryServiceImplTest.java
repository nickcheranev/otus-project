package ru.ncheranev.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ncheranev.otus.jpa.domain.Category;
import ru.ncheranev.otus.jpa.repository.CategoryRepository;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("класс CategoryServiceImpl")
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl sut;

    @Test
    @DisplayName("должен завершить выполнение при пустом аргументе")
    void shouldReturnWhenEmptyArgument() {
        // given
        var names = List.<String>of();

        // when
        sut.createAbsentByNames(names);

        // then
        verify(categoryRepository, never()).findAllByNameIn(names);
        verify(categoryRepository, never()).saveAll(any());
    }

    @Test
    @DisplayName("должен создать отсутствующие категории")
    void shouldCreateAbsentByNamesCategories() {
        // given
        var names = List.of("name1", "name2");
        var present = List.of(new Category().setName("name1"));
        when(categoryRepository.findAllByNameIn(names)).thenReturn(present);

        // when
        sut.createAbsentByNames(names);

        // then
        verify(categoryRepository).findAllByNameIn(names);
        verify(categoryRepository).saveAll(List.of(new Category().setName("name2")));
    }
}

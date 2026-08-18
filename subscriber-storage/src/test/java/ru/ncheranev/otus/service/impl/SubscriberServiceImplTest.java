package ru.ncheranev.otus.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ncheranev.otus.dto.SubscriberDto;
import ru.ncheranev.otus.jpa.domain.Category;
import ru.ncheranev.otus.jpa.domain.Subscriber;
import ru.ncheranev.otus.jpa.repository.SubscriberRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriberServiceImplTest {

    @Mock
    private SubscriberRepository subscriberRepository;
    @InjectMocks
    private SubscriberServiceImpl sut;

    @Test
    void findByCategories_returnsDtoList() {
        // given
        List<String> categories = List.of("ai", "java");
        Subscriber sub1 = new Subscriber();
        sub1.setId(1L);
        sub1.setName("Alice");
        sub1.setEmail("alice@test.com");
        sub1.setCategories(List.of(new Category().setName("ai")));

        Subscriber sub2 = new Subscriber();
        sub2.setId(2L);
        sub2.setName("Bob");
        sub2.setEmail("bob@test.com");
        sub2.setCategories(List.of(new Category().setName("java")));

        when(subscriberRepository.findByCategoriesNameIn(categories))
                .thenReturn(List.of(sub1, sub2));

        // when
        List<SubscriberDto> result = sut.findByCategories(categories);

        // then
        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
        assertEquals("Bob", result.get(1).getName());
        verify(subscriberRepository).findByCategoriesNameIn(categories);
    }

    @Test
    void findByCategories_emptyResult_returnsEmptyList() {
        // given
        List<String> categories = List.of("unknown");
        when(subscriberRepository.findByCategoriesNameIn(categories))
                .thenReturn(List.of());

        // when
        List<SubscriberDto> result = sut.findByCategories(categories);

        // then
        assertTrue(result.isEmpty());
        verify(subscriberRepository).findByCategoriesNameIn(categories);
    }
}
package ru.ncheranev.otus.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.ncheranev.otus.dto.SubscriberDto;
import ru.ncheranev.otus.jpa.domain.Category;
import ru.ncheranev.otus.jpa.domain.Subscriber;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("класс MapperUtil")
class MapperUtilTest {

    @Test
    @DisplayName("должен маппить из entity в dto")
    void shouldMappingSubscriberEntityToSubscriberDto() {
        // given
        Subscriber subscriber = new Subscriber()
                .setId(1L)
                .setName("name")
                .setEmail("email")
                .setCategories(List.of(new Category().setId(1L).setName("category")));

        // when
        var actual = MapperUtil.toDto(subscriber);

        // then
        assertThat(actual).isNotNull()
                .usingRecursiveAssertion()
                .isEqualTo(new SubscriberDto()
                        .setId(1L)
                        .setName("name")
                        .setEmail("email")
                        .setCategories(List.of("category")));
    }
}
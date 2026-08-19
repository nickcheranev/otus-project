package ru.ncheranev.otus.util;

import lombok.experimental.UtilityClass;
import ru.ncheranev.otus.dto.ArticleDto;
import ru.ncheranev.otus.dto.EntryDto;
import ru.ncheranev.otus.jpa.domain.Article;
import ru.ncheranev.otus.jpa.domain.Category;

import static java.util.Objects.isNull;

@UtilityClass
public class MapperUtil {
    public static Article toEntity(ArticleDto dto) {
        return new Article()
                .setId(dto.getId())
                .setUri(dto.getUri())
                .setTitle(dto.getTitle())
                .setLink(dto.getLink())
                .setDescription(dto.getDescription())
                .setPublishDate(dto.getPublishDate())
                .setAuthor(dto.getAuthor())
                .setSource(dto.getSource());
    }

    public static ArticleDto toArticle(EntryDto entry) {
        return new ArticleDto()
                .setUri(entry.getUri())
                .setTitle(entry.getTitle())
                .setLink(entry.getLink())
                .setDescription(entry.getDescription())
                .setPublishDate(entry.getPublishDate())
                .setAuthor(entry.getAuthor())
                .setCategories(entry.getCategories())
                .setSource(entry.getSource());
    }

    public static ArticleDto toDto(Article entity) {
        return new ArticleDto()
                .setId(entity.getId())
                .setUri(entity.getUri())
                .setTitle(entity.getTitle())
                .setLink(entity.getLink())
                .setDescription(entity.getDescription())
                .setPublishDate(entity.getPublishDate())
                .setAuthor(entity.getAuthor())
                .setCategories(isNull(entity.getCategories()) ? null : entity.getCategories()
                        .stream().map(Category::getName).toList())
                .setSource(entity.getSource());
    }
}

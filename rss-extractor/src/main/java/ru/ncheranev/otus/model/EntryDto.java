package ru.ncheranev.otus.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * RSS feed entry
 */
@Data
@Accessors(chain = true)
public class EntryDto {
    private String uri;
    private String title;
    private String link;
    private String description;
    private LocalDateTime publishDate;
    private String author;
    private Set<String> categories;
    private String source;
}
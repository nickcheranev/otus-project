package ru.ncheranev.otus.jpa.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * RSS feed
 */
@Entity
@Data
@EqualsAndHashCode(of = "uri")
@Accessors(chain = true)
public class Entry implements Serializable {
    @Id
    private String uri;
    private String title;
    private String link;
    @Lob
    private String description;
    private LocalDateTime publishDate;
    private String author;
    private Set<String> categories;
    private String source;
}

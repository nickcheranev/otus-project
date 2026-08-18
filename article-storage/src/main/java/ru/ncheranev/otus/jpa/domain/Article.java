package ru.ncheranev.otus.jpa.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(of = "id")
@Accessors(chain = true)
@SequenceGenerator(name = "article_seq", sequenceName = "article_seq", allocationSize = 1)
public class Article implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_seq")
    @Column(nullable = false)
    private Long id;
    @Column(unique = true)
    private String uri;
    @Column(nullable = false)
    private String title;
    @Column(nullable=false)
    private String link;
    @Lob
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private LocalDateTime publishDate;
    @Column(nullable = false)
    private String author;
    @ManyToMany
    @JoinTable(name = "article_category",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;
    private String source;
}

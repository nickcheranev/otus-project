package ru.ncheranev.otus.jpa.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(of = "id")
@Accessors(chain = true)
public class Subscriber implements Serializable {
    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subscriber_seq")
    private Long id;
    /**
     * Имя
     */
    @Column(unique = true)
    private String name;
    /**
     * Email
     */
    private String email;
    /**
     * Список категорий
     */
    @ManyToMany
    @JoinTable(name = "subscriber_category",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;
}

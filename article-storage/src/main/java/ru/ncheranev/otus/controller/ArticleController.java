package ru.ncheranev.otus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ncheranev.otus.dto.ArticleDto;
import ru.ncheranev.otus.service.ArticleService;

import java.util.List;

/**
 * REST контроллер для работы со статьями
 */
@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    /**
     * Получить все статьи
     *
     * @return список статей
     */
    @GetMapping
    public List<ArticleDto> getAll() {
        return articleService.findAll();
    }

    /**
     * Получить статью по id
     *
     * @param id id статьи
     * @return статья
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto> getById(@PathVariable Long id) {
        return ResponseEntity.of(articleService.findById(id));
    }
}

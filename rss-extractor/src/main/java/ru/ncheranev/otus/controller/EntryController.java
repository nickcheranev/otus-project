package ru.ncheranev.otus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ncheranev.otus.controller.dto.UriInfo;
import ru.ncheranev.otus.model.EntryDto;
import ru.ncheranev.otus.service.EntryService;

/**
 * REST контроллер для работы с записями RSS feed
 */
@RestController
@RequestMapping("/entry")
@RequiredArgsConstructor
@Slf4j
public class EntryController {
    private final EntryService entryService;

    /**
     * Получить запись по URI
     *
     * @param info URI
     * @return запись
     */
    @GetMapping
    public ResponseEntity<EntryDto> get(@RequestBody UriInfo info) {
        var optArticle = entryService.findByUri(info.getUri());
        optArticle.ifPresent((article -> log.info("Найдена статья: {}", article)));
        return optArticle.map(article -> ResponseEntity.ok(optArticle.get()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

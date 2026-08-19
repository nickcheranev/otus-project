package ru.ncheranev.otus.service;

import ru.ncheranev.otus.model.EntryDto;

import java.util.Optional;

/**
 * Сервис для работы с записями RSS ленты
 */
public interface EntryService {
    Optional<EntryDto> findByUri(String uri);
}

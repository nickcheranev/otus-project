package ru.ncheranev.otus.service;

import ru.ncheranev.otus.model.EntryDto;

import java.util.Optional;

public interface EntryService {
    Optional<EntryDto> findByUri(String uri);
}

package ru.ncheranev.otus.service;

import org.springframework.http.HttpHeaders;
import ru.ncheranev.otus.dto.EntryDto;

public interface EntryProvider {
    EntryDto getEntry(String uri, HttpHeaders headers);
}

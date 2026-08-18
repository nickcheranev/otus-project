package ru.ncheranev.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ncheranev.otus.jpa.repository.EntryRepository;
import ru.ncheranev.otus.model.EntryDto;
import ru.ncheranev.otus.service.EntryService;
import ru.ncheranev.otus.util.MapperUtil;

import java.util.Optional;

/**
 * Сервис для работы с записями RSS ленты
 */
@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {
    private final EntryRepository entryRepository;

    /**
     * Найти запись по URI
     *
     * @param uri URI записи
     * @return Запись
     */
    @Override
    public Optional<EntryDto> findByUri(String uri) {
        var optEntry = entryRepository.findByUri(uri);
        return optEntry.map(MapperUtil::toDto);
    }
}

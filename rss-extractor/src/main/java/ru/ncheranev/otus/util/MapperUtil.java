package ru.ncheranev.otus.util;

import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndEntry;
import lombok.experimental.UtilityClass;
import ru.ncheranev.otus.jpa.domain.Entry;
import ru.ncheranev.otus.model.EntryDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@UtilityClass
public class MapperUtil {

    public static Entry toEntity(EntryDto entryDto) {
        return new Entry()
                .setUri(entryDto.getUri())
                .setTitle(entryDto.getTitle())
                .setLink(entryDto.getLink())
                .setDescription(entryDto.getDescription())
                .setPublishDate(entryDto.getPublishDate())
                .setAuthor(entryDto.getAuthor())
                .setCategories(entryDto.getCategories())
                .setSource(entryDto.getSource());
    }

    public static EntryDto toDto(Entry entry) {
        return new EntryDto()
                .setUri(entry.getUri())
                .setTitle(entry.getTitle())
                .setLink(entry.getLink())
                .setDescription(entry.getDescription())
                .setPublishDate(entry.getPublishDate())
                .setAuthor(entry.getAuthor())
                .setCategories(entry.getCategories())
                .setSource(entry.getSource());
    }

    public static EntryDto toDto(SyndEntry syndEntry) {
        String description = null;
        if(nonNull(syndEntry.getDescription())) {
            description = syndEntry.getDescription().getValue();
        }
        LocalDateTime publishDate = null;
        if(nonNull(syndEntry.getPublishedDate())) {
            publishDate = Instant.ofEpochMilli(syndEntry.getPublishedDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        String source = null;
        if(nonNull(syndEntry.getSource())) {
            source = syndEntry.getSource().toString();
        }
        return new EntryDto()
                .setUri(syndEntry.getUri())
                .setTitle(syndEntry.getTitle())
                .setLink(syndEntry.getLink())
                .setDescription(description)
                .setPublishDate(publishDate)
                .setAuthor(syndEntry.getAuthor())
                .setCategories(syndEntry.getCategories().stream().map(SyndCategory::getName).collect(Collectors.toSet()))
                .setSource(source)
                ;
    }
}

package ru.ncheranev.otus.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * URI
 */
@Data
@Accessors(chain = true)
public class UriInfo {
    private String uri;
}

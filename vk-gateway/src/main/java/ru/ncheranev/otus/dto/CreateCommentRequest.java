package ru.ncheranev.otus.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Форматированное сообщение для создания комментария
 */
@Data
@Accessors(chain = true)
public class CreateCommentRequest {
    /**
     * Текст сообщения
     */
    private String message;
}

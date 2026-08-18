package ru.ncheranev.otus.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Ответ на создание комментария
 */
@Data
@Accessors(chain = true)
public class CreateCommentResponse {
    private Response response;
    @Data
    @Accessors(chain = true)
    public static class Response {
        private String comment_id;
        private Object[] parents_stack;
    }
}

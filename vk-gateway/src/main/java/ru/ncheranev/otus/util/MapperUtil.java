package ru.ncheranev.otus.util;

import lombok.experimental.UtilityClass;
import ru.ncheranev.otus.dto.CommunicatorDto;
import ru.ncheranev.otus.dto.CreateCommentRequest;

@UtilityClass
public class MapperUtil {
    public static CreateCommentRequest toCreateComment(CommunicatorDto dto) {
        return new CreateCommentRequest()
                .setMessage(
                        dto.getArticle().getTitle() + "\n" +
                        dto.getArticle().getUri() + "\n" +
                        dto.getArticle().getCategories());
    }
}

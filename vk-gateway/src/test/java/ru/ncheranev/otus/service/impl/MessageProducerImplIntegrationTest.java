package ru.ncheranev.otus.service.impl;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.ncheranev.otus.dto.CreateCommentRequest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "app.vk.create-comment-method-url = https://api.vk.com/method/wall.createComment?v=5.199",
        "app.vk.access-token = ${APP_VK_ACCESS_TOKEN:dummy}",
        "app.vk.post-id = 16",
        "app.vk.owner-id = -237133367"
})
@Disabled("not a integration test")
class MessageProducerImplIntegrationTest {
    @Autowired
    private MessageProducerImpl sut;

    @Test
    void wallCreateComment_v1() {
        // given
        var request = new CreateCommentRequest().setMessage("test message: " + LocalDateTime.now());

        // when
        assertThat(sut.wallCreateComment_v1(request)).isTrue();
    }

    @Test
    void wallCreateComment_v2() {
        // given
        var request = new CreateCommentRequest().setMessage("test message (feign): " + LocalDateTime.now());

        // when
        assertThat(sut.wallCreateComment_v2(request)).isTrue();
    }
}
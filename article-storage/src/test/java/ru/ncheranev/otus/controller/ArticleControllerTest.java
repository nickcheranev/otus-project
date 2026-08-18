package ru.ncheranev.otus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.ncheranev.otus.dto.ArticleDto;
import ru.ncheranev.otus.service.ArticleService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ArticleController integration tests")
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ArticleService articleService;

    private ArticleDto articleDto1;
    private ArticleDto articleDto2;

    @BeforeEach
    void setUp() {
        articleDto1 = new ArticleDto()
                .setId(1L)
                .setTitle("article 1")
                .setAuthor("author 1");

        articleDto2 = new ArticleDto()
                .setId(2L)
                .setTitle("article 2")
                .setAuthor("author 2");
    }

    @Test
    @WithMockUser
    @DisplayName("GET /article - должен вернуть все статьи")
    void getAll_ShouldReturnArticlesList() throws Exception {
        // Arrange
        List<ArticleDto> articles = Arrays.asList(articleDto1, articleDto2);
        when(articleService.findAll()).thenReturn(articles);

        // Act & Assert
        mockMvc.perform(get("/article")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("article 1"))
                .andExpect(jsonPath("$[0].author").value("author 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("article 2"))
                .andExpect(jsonPath("$[1].author").value("author 2"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /article/{id} - должен вернуть статью при наличии")
    void getById_WhenArticleExists_ShouldReturnArticle() throws Exception {
        // Arrange
        Long articleId = 1L;
        when(articleService.findById(articleId)).thenReturn(Optional.of(articleDto1));

        // Act & Assert
        mockMvc.perform(get("/article/{id}", articleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("article 1"))
                .andExpect(jsonPath("$.author").value("author 1"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /article/{id} - должен возвращать 404 при отсутствии статьи с таким id")
    void getById_WhenArticleNotFound_ShouldReturn404() throws Exception {
        // Arrange
        Long articleId = 999L;
        when(articleService.findById(articleId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/article/{id}", articleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

package ru.ncheranev.otus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.ncheranev.otus.controller.dto.UriInfo;
import ru.ncheranev.otus.jpa.repository.EntryRepository;
import ru.ncheranev.otus.model.EntryDto;
import ru.ncheranev.otus.service.impl.EntryServiceImpl;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EntryController.class)
@DisplayName("EntryController integration tests")
class EntryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EntryServiceImpl entryService;

    @MockitoBean(name = "entityManagerFactory")
    private EntityManagerFactory entityManagerFactory;

    @MockitoBean(name = "entryRepository")
    private EntryRepository entryRepository;

    private UriInfo uriInfo;
    private EntryDto entry;

    @BeforeEach
    void setUp() {
        uriInfo = new UriInfo();
        uriInfo.setUri("http://example.com/test");

        entry = new EntryDto();
        entry.setUri("http://example.com/test");
        entry.setTitle("Test Article");
    }

    @Test
    @DisplayName("GET /entry - должен вернуть 200 OK когда запись существует")
    @WithMockUser(username = "testuser")
    void getEntry_WhenArticleExists_ShouldReturn200() throws Exception {
        // Arrange
        when(entryService.findByUri(anyString())).thenReturn(Optional.of(entry));

        // Act & Assert
        mockMvc.perform(get("/entry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(uriInfo)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /entry - должен возвращать 404 Not found при отсутствии записи")
    @WithMockUser(username = "testuser")
    void getEntry_WhenArticleDoesNotExist_ShouldReturn404() throws Exception {
        // Arrange
        when(entryService.findByUri(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/entry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(uriInfo)))
                .andExpect(status().isNotFound());
    }
}
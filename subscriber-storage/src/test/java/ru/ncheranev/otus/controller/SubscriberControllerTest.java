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
import ru.ncheranev.otus.dto.CategoryInfo;
import ru.ncheranev.otus.dto.SubscriberDto;
import ru.ncheranev.otus.service.SubscriberService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("SubscriberController integration tests")
class SubscriberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SubscriberService subscriberService;

    private CategoryInfo categoryInfo;
    private List<SubscriberDto> subscriberDtos;

    @BeforeEach
    void setUp() {
        categoryInfo = new CategoryInfo();
        categoryInfo.setNames(Arrays.asList("Sports", "News"));

        SubscriberDto dto1 = new SubscriberDto()
                .setId(1L)
                .setName("John Doe")
                .setEmail("john@example.com")
                .setCategories(Arrays.asList("Sports", "News"));

        SubscriberDto dto2 = new SubscriberDto()
                .setId(2L)
                .setName("Jane Smith")
                .setEmail("jane@example.com")
                .setCategories(List.of("News"));

        subscriberDtos = Arrays.asList(dto1, dto2);
    }

    @Test
    @WithMockUser
    @DisplayName("GET /subscriber - integration test")
    void getAllByCategoryNames_integrationTest() throws Exception {
        // Arrange
        when(subscriberService.findByCategories(anyList())).thenReturn(subscriberDtos);

        // Act & Assert
        mockMvc.perform(get("/subscriber")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"));
    }
}
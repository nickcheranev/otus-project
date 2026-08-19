package ru.ncheranev.otus.service.impl;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import ru.ncheranev.otus.config.AppProperties;
import ru.ncheranev.otus.dto.ArticleDto;
import ru.ncheranev.otus.jpa.domain.Article;
import ru.ncheranev.otus.jpa.domain.Category;
import ru.ncheranev.otus.jpa.repository.ArticleRepository;
import ru.ncheranev.otus.jpa.repository.CategoryRepository;
import ru.ncheranev.otus.service.CategoryService;
import ru.ncheranev.otus.util.MapperUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("тестирование сервиса для работы с статьями ArticleServiceImpl")
class ArticleServiceImplTest {
    private final LocalDateTime PUBLISH_DATE_TIME = LocalDateTime.now();
    ArticleDto ARTICLE_DTO = new ArticleDto()
            .setId(1L)
            .setAuthor("author")
            .setDescription("description")
            .setTitle("title")
            .setLink("link")
            .setUri("uri")
            .setPublishDate(PUBLISH_DATE_TIME);
    private final Article ARTICLE_ENTITY = MapperUtil.toEntity(ARTICLE_DTO);
    private final AppProperties APP_PROPERTIES = new AppProperties()
            .setEvents(Map.of(
                    "rss-feed-received", new AppProperties.Event().setTopic("rss-feed-received-topic"),
                    "article-created", new AppProperties.Event().setTopic("article-created-topic")));

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryService categoryService;
    @Mock
    private KafkaTemplate<String, Long> kafkaTemplate;
    @InjectMocks
    private ArticleServiceImpl sut;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(sut, "appProperties", APP_PROPERTIES);
    }

    @Test
    @DisplayName("должен создать статью при ее отсутствии (по uri) и вернуть её DTO")
    void shouldCreateArticleWhenAbsent() {
        // given
        when(articleRepository.findByUri("uri")).thenReturn(Optional.empty());
        when(articleRepository.save(MapperUtil.toEntity(ARTICLE_DTO))).thenReturn(ARTICLE_ENTITY);

        // when
        var actual = sut.create(ARTICLE_DTO);

        // then
        assertThat(actual).usingRecursiveAssertion().isEqualTo(ARTICLE_DTO);
    }

    @Test
    @DisplayName("должен пропустить создание статьи при ее наличии")
    void shouldSkipCreateArticleWhenExists() {
        // given
        when(articleRepository.findByUri(ARTICLE_DTO.getUri())).thenReturn(Optional.of(ARTICLE_ENTITY));

        // when
        var actual = sut.create(ARTICLE_DTO);

        // then
        assertThat(actual).usingRecursiveAssertion().isEqualTo(ARTICLE_DTO);
        verify(articleRepository, never()).save(any(Article.class));
    }

    @Test
    @DisplayName("должен найти все статьи")
    void shouldFindAllArticles() {
        // given
        when(articleRepository.findAll()).thenReturn(List.of(ARTICLE_ENTITY));

        // when
        var result = sut.findAll();

        // then
        assertThat(result).asInstanceOf(InstanceOfAssertFactories.LIST).hasSize(1);
    }

    @Test
    @DisplayName("должен найти статью по идентификатору")
    void shouldFindArticleById() {
        // given
        when(articleRepository.findById(1L)).thenReturn(Optional.of(ARTICLE_ENTITY));

        // when
        var result = sut.findById(1L);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.orElseThrow().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("должен выбросить исключение, если статья не найдена")
    void shouldThrowNoSuchElementExceptionWhenAbsentArticle() {
        // given
        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> sut.findById(1L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("должен найти статью по полю uri")
    void shouldFindArticleByUri() {
        // given
        when(articleRepository.findByUri("uri")).thenReturn(Optional.of(ARTICLE_ENTITY));

        // when
        var result = sut.findByUri("uri");

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.orElseThrow().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("должен присвоить статье категории")
    void shouldCategorizeArticle() {
        // given
        var categoryNames = List.of("category");
        var categories = categoryNames.stream().map(i -> new Category().setName(i)).toList();
        when(categoryRepository.findAllByNameIn(categoryNames)).thenReturn(categories);
        var article = new Article().setId(1L);
        when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));
        when(articleRepository.save(article)).thenReturn(article);

        // when
        var actual = sut.categorize(1L, List.of("category"));

        // then
        assertThat(actual.getCategories()).asInstanceOf(InstanceOfAssertFactories.LIST).hasSize(1);
        assertThat(actual.getCategories().get(0)).isEqualTo("category");
        verify(categoryService).createAbsentByNames(categoryNames);
    }

    @Captor
    private ArgumentCaptor<ProducerRecord<String, Long>> producerRecordCaptor;

    @Test
    @DisplayName("должен опубликовать статью в Kafka")
    void shouldPublishArticleToKafka() {
        // when
        sut.publishToKafka(1L);

        // then
        verify(kafkaTemplate).send(producerRecordCaptor.capture());
        assertThat(producerRecordCaptor.getValue().topic()).isEqualTo("article-created-topic");
    }
}

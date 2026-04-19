package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ru.yandex.practicum.controller.CommentController;
import ru.yandex.practicum.controller.PostController;
import ru.yandex.practicum.controller.PostImageController;
import ru.yandex.practicum.controller.PostLikesController;
import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.dao.PostDao;
import ru.yandex.practicum.dao.PostImageStorage;
import ru.yandex.practicum.dao.impl.H2CommentDaoImpl;
import ru.yandex.practicum.dao.impl.H2PostDaoImpl;
import ru.yandex.practicum.dao.impl.LocalFileStorage;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.PostService;

import javax.sql.DataSource;

@Configuration
public class TestConfig {

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:db/schema.sql")
                .build();

    }

    @Bean
    public NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public CommentDao commentDao() {
        return new H2CommentDaoImpl(jdbcTemplate(dataSource()));
    }

    @Bean
    public CommentService commentService() {
        return new CommentService(commentDao());
    }

    @Bean
    public PostDao postDao() {
        return new H2PostDaoImpl(jdbcTemplate(dataSource()));
    }

    @Bean
    PostImageStorage postImageStorage() {
        return new LocalFileStorage();
    }

    @Bean
    public PostService postService() {
        return new PostService(postDao(), postImageStorage());
    }

    @Bean
    public CommentController commentController() {
        return new CommentController(commentService());
    }

    @Bean
    public PostLikesController postLikesController() {
        return new PostLikesController(postService());
    }

    @Bean
    public PostImageController postImageController() {
        return new PostImageController(postService());
    }

    @Bean
    public PostController postController() {
        return new PostController(postService());
    }



}

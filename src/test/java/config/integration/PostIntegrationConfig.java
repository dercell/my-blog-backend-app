package config.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.yandex.practicum.dao.PostDao;
import ru.yandex.practicum.dao.PostImageStorage;
import ru.yandex.practicum.dao.impl.H2PostDaoImpl;
import ru.yandex.practicum.dao.impl.LocalFileStorage;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.PostService;

@Configuration
@Import({CommentIntegrationConfig.class, DataSourceIntegrationConfig.class})
public class PostIntegrationConfig {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private CommentService commentService;

    @Bean
    public PostDao postDao() {
        return new H2PostDaoImpl(namedParameterJdbcTemplate);
    }

    @Bean
    PostImageStorage postImageStorage() {
        return new LocalFileStorage();
    }

    @Bean
    public PostService postService() {
        return new PostService(postDao(), postImageStorage(), commentService);
    }


}

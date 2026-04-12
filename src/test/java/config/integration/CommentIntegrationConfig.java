package config.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.dao.impl.H2CommentDaoImpl;
import ru.yandex.practicum.service.CommentService;

@Configuration
@Import(DataSourceIntegrationConfig.class)
public class CommentIntegrationConfig {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Bean
    public CommentDao commentDao() {
        return new H2CommentDaoImpl(namedParameterJdbcTemplate);
    }

    @Bean
    public CommentService commentService() {
        return new CommentService(commentDao());
    }

}

package config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.service.CommentService;

@Configuration
public class CommentUnitConfig {

    @Bean
    public CommentDao commentDao(){
        return Mockito.mock(CommentDao.class);
    }

    @Bean
    public CommentService commentService() {
        return new CommentService(commentDao());
    }

}

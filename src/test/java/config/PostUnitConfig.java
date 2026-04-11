package config;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.dao.PostDao;
import ru.yandex.practicum.dao.PostImageStorage;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.PostService;

@Configuration
@Import(CommentUnitConfig.class)
public class PostUnitConfig {

    @Autowired
    private CommentService commentService;

    @Bean
    public PostDao postDao() {
        return Mockito.mock(PostDao.class);
    }

    @Bean
    PostImageStorage postImageStorage() {
        return Mockito.mock(PostImageStorage.class);
    }

    @Bean
    public PostService postService() {
        return new PostService(postDao(), postImageStorage(), commentService);
    }


}

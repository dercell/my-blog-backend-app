package config.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.controller.PostController;
import ru.yandex.practicum.controller.PostImageController;
import ru.yandex.practicum.controller.PostLikesController;
import ru.yandex.practicum.service.PostService;

@Configuration
@Import(PostIntegrationConfig.class)
public class PostControllerConfig {

    @Autowired
    private PostService postService;

    @Bean
    public PostController postController() {
        return new PostController(postService);
    }

    @Bean
    public PostImageController postImageController(){
        return new PostImageController(postService);
    }

    @Bean
    public PostLikesController postLikesController(){
        return new PostLikesController(postService);
    }

}

package config.integration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.controller.PostLikesController;
import ru.yandex.practicum.service.PostService;

@Configuration
@Import(PostIntegrationConfig.class)
public class PostLikesControllerConfig {

    @Autowired
    private PostService postService;

    @Bean
    public PostLikesController postLikesController() {
        return new PostLikesController(postService);
    }
}

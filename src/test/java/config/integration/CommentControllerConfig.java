package config.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.controller.CommentController;
import ru.yandex.practicum.service.CommentService;

@Configuration
@Import(CommentIntegrationConfig.class)
public class CommentControllerConfig {

    @Autowired
    private CommentService commentService;

    @Bean
    public CommentController commentController() {
        return new CommentController(commentService);
    }


}

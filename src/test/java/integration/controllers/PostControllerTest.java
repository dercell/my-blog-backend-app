package integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.TestConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.controller.PostController;
import ru.yandex.practicum.model.PostCreateRequest;
import ru.yandex.practicum.model.PostResponse;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("rest")
@Tag("integration")
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class PostControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private PostController postController;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final ObjectMapper om = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
        namedParameterJdbcTemplate.getJdbcTemplate().execute("RUNSCRIPT FROM 'classpath:db/data.sql'");
    }

    @AfterEach
    void cleanUp() {
        namedParameterJdbcTemplate.getJdbcTemplate().execute("RUNSCRIPT FROM 'classpath:db/cleanup.sql'");
    }

    @Test
    void getPosts() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .param("search", "")
                        .param("pageNumber", "1")
                        .param("pageSize", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.posts").isArray())
                .andExpect(jsonPath("$.posts.length()").value(2));
    }

    @Test
    void getPostById() throws Exception {
        mockMvc.perform(get("/api/posts/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.title").value("Первый пост"));
    }

    @Test
    void savePost() throws Exception {
        PostCreateRequest newPost = PostCreateRequest.builder().title("Третий пост").text("Текст третьего поста").tags(List.of("tag3", "tag5")).build();
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(newPost)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.title").value(newPost.getTitle()));
    }


    @Test
    void updatePost() throws Exception {
        PostResponse updatePostResponse = PostResponse.builder().title("Третий пост").text("Новый текст второго поста").tags(List.of("tag5")).build();
        mockMvc.perform(put("/api/posts/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(updatePostResponse)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.title").value(updatePostResponse.getTitle()))
                .andExpect(jsonPath("$.text").value(updatePostResponse.getText()))
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags.length()").value(1));
    }

    @Test
    void deletePost() throws Exception {
        mockMvc.perform(delete("/api/posts/{id}", 2))
                .andExpect(status().isOk());
    }

}

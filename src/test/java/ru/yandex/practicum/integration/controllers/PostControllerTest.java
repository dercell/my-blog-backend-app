package ru.yandex.practicum.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.model.PostRequestDto;
import ru.yandex.practicum.model.PostResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("rest")
@Tag("integration")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final ObjectMapper om = new ObjectMapper();

    @BeforeEach
    void setUp() {
        namedParameterJdbcTemplate.getJdbcTemplate().execute("RUNSCRIPT FROM 'classpath:db/data.sql'");
    }

    @AfterEach
    void cleanUp() {
        namedParameterJdbcTemplate.getJdbcTemplate().execute("RUNSCRIPT FROM 'classpath:db/cleanup.sql'");
    }

    static Stream<Arguments> invalidPosts() {
        return Stream.of(
                Arguments.of(PostRequestDto.builder().id(1L).title(null).text("text").tags(List.of("t1")).build(), "title", "Не может быть пустым"),
                Arguments.of(PostRequestDto.builder().id(2L).title("title").text(null).tags(List.of("t1")).build(), "text", "Не может быть пустым"),
                Arguments.of(PostRequestDto.builder().id(3L).title("title").text("text").tags(null).build(), "tags", "Не может быть пустым"),
                Arguments.of(PostRequestDto.builder().id(4L).title("title").text("text").tags(new ArrayList<>()).build(), "tags", "Не может быть пустым")
        );
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
        PostRequestDto newPost = PostRequestDto.builder().title("Третий пост").text("Текст третьего поста").tags(List.of("tag3", "tag5")).build();
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
        PostResponseDto updatePostResponse = PostResponseDto.builder().title("Третий пост").text("Новый текст второго поста").tags(List.of("tag5")).build();
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

    @ParameterizedTest
    @MethodSource("invalidPosts")
    void invalidUpdating(PostRequestDto p, String invalidField, String errorText) throws Exception {
        System.out.println("PUT -> " + om.writeValueAsString(p));
        mockMvc.perform(put("/api/posts/{id}", p.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(p)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$." + invalidField).value(errorText));
    }

    @ParameterizedTest
    @MethodSource("invalidPosts")
    void invalidCreatePosts(PostRequestDto p, String invalidField, String errorText) throws Exception {
        System.out.println("POST -> " + om.writeValueAsString(p));
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(p)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$." + invalidField).value(errorText));

    }

}

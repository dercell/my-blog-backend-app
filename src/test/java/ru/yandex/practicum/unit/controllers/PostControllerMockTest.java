package ru.yandex.practicum.unit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.controller.PostController;
import ru.yandex.practicum.model.PagePostResponse;
import ru.yandex.practicum.model.PostRequestDto;
import ru.yandex.practicum.model.PostResponseDto;
import ru.yandex.practicum.service.PostService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("unit")
@Tag("rest")
@WebMvcTest(PostController.class)
class PostControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final ObjectMapper om = new ObjectMapper();

    @Test
    void getPosts() throws Exception {

        PagePostResponse ppr = new PagePostResponse(
                List.of(PostResponseDto.builder().id(1L).title("Post #1").text("Post text 1").tags(List.of("tag1")).likesCount(0).commentsCount(0).build(),
                        PostResponseDto.builder().id(2L).title("Post #2").text("Post text 2").tags(List.of("tag1", "tag2")).likesCount(10).commentsCount(5).build(),
                        PostResponseDto.builder().id(3L).title("Post #3").text("Post text 3").tags(List.of("tag2", "tag3")).likesCount(123).commentsCount(3).build()),
                false, false, 1
        );

        when(postService.getPosts("", 1, 5)).thenReturn(ppr);

        mockMvc.perform(get("/api/posts")
                        .param("search", "")
                        .param("pageNumber", "1")
                        .param("pageSize", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.posts").isArray())
                .andExpect(jsonPath("$.posts.length()").value(3));
    }

    @Test
    void getPostById() throws Exception {

        PostResponseDto p = PostResponseDto.builder().id(1L).title("Post #1").text("Post text 1").tags(List.of("tag1")).likesCount(0).commentsCount(0).build();
        when(postService.getPostById(1L)).thenReturn(Optional.of(p));

        mockMvc.perform(get("/api/posts/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.title").value("Post #1"));
    }

    @Test
    void savePost() throws Exception {
        PostRequestDto newPost = PostRequestDto.builder().title("Третий пост").text("Текст третьего поста").tags(List.of("tag3", "tag5")).build();


        PostResponseDto response = PostResponseDto.builder()
                .id(2L).title("Третий пост").text("Текст третьего поста").tags(List.of("tag3", "tag5")).build();

        when(postService.savePost(newPost)).thenReturn(Optional.ofNullable(response));

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

        PostRequestDto p = PostRequestDto.builder()
                .id(2L).title("Post #2").text("Post text 2").tags(List.of("tag1", "tag2")).build();

        PostResponseDto response = PostResponseDto.builder()
                .id(2L).title("Post #2").text("Post text 2").tags(List.of("tag1", "tag2")).build();

        when(postService.updatePost(p, 2L)).thenReturn(Optional.of(response));

        mockMvc.perform(put("/api/posts/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(response)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.title").value(response.getTitle()))
                .andExpect(jsonPath("$.text").value(response.getText()))
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags.length()").value(2));
    }

    @Test
    void deletePost() throws Exception {
        mockMvc.perform(delete("/api/posts/{id}", 2))
                .andExpect(status().isOk());
    }

}

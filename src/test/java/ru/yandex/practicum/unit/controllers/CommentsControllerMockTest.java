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
import ru.yandex.practicum.controller.CommentController;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.service.CommentService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("rest")
@Tag("unit")
@WebMvcTest(CommentController.class)
class CommentsControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentService commentService;

    @MockitoBean
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final ObjectMapper om = new ObjectMapper();


    @Test
    void findAllByPostId() throws Exception {

        when(commentService.findAllByPostId(1L)).thenReturn(
                List.of(Comment.builder().id(1L).text("Первый коммент").postId(1L).build(),
                        Comment.builder().id(2L).text("Второй коммент").postId(1L).build())

        );

        mockMvc.perform(get("/api/posts/{postId}/comments", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

    }

    @Test
    void getPostById() throws Exception {

        when(commentService.getById(1L, 2L)).thenReturn(
                Optional.of(Comment.builder().id(2L).text("Согласен!").postId(1L).build()));

        mockMvc.perform(get("/api/posts/{postId}/comments/{id}", 1L, 2L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text").value("Согласен!"));

    }

    @Test
    void saveComment() throws Exception {
        Comment newComment = Comment.builder().text("Новый коммент №3").postId(1L).build();
        when(commentService.saveComment(1L, newComment))
                .thenReturn(Optional.of(newComment));


        mockMvc.perform(post("/api/posts/{postId}/comments", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(newComment)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text").value(newComment.getText()));
    }

    @Test
    void updateComment() throws Exception {
        Comment updatedComment = Comment.builder().id(2L).text("Измененный коммент").postId(1L).build();

        when(commentService.updateComment(1L, 2L, updatedComment))
                .thenReturn(Optional.of(updatedComment));

        mockMvc.perform(put("/api/posts/{postId}/comments/{id}", 1L, 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(updatedComment)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text").value(updatedComment.getText()));
    }

    @Test
    void deleteComment() throws Exception {
        mockMvc.perform(delete("/api/posts/{postId}/comments/{id}", 1L, 1L))
                .andExpect(status().isOk());
    }

}

package ru.yandex.practicum.unit.controllers;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.controller.PostImageController;
import ru.yandex.practicum.service.PostService;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("rest")
@Tag("unit")
@WebMvcTest(PostImageController.class)
class PostImageControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Test
    void uploadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "image.png", "image/png", new byte[]{1, 2, 3, 4});

        doNothing().when(postService).uploadPostImage(1L, file);

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/posts/{id}/image", 1).file(file))
                .andExpect(status().isOk());
    }

    @Test
    void downloadFile() throws Exception {

        MockMultipartFile file = new MockMultipartFile("image", "image.png", "image/png", new byte[]{1, 2, 3, 4});

        when(postService.downloadPostImage(1L)).thenReturn(
                new ByteArrayResource(file.getBytes())
        );

        MvcResult result = mockMvc.perform(get("/api/posts/{id}/image", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();

        byte[] returnFile = result.getResponse().getContentAsByteArray();
        assertArrayEquals(file.getBytes(), returnFile);

    }

}

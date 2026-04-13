package integration.controllers;

import config.TestConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.controller.PostImageController;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("rest")
@Tag("integration")
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class PostImageControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private PostImageController postImageController;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(postImageController).build();
        namedParameterJdbcTemplate.getJdbcTemplate().execute("RUNSCRIPT FROM 'classpath:db/data.sql'");
    }

    @AfterEach
    void cleanUp() {
        namedParameterJdbcTemplate.getJdbcTemplate().execute("RUNSCRIPT FROM 'classpath:db/cleanup.sql'");
    }

    @Test
    void uploadAndDownloadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "image.png", "image/png", new byte[]{1, 2, 3, 4});

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/posts/{id}/image", 1).file(file))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/api/posts/{id}/image", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();

        byte[] returnFile = result.getResponse().getContentAsByteArray();
        assertArrayEquals(file.getBytes(), returnFile);

    }

}

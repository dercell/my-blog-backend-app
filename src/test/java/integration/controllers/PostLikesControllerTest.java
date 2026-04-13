package integration.controllers;

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
import ru.yandex.practicum.controller.PostLikesController;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("rest")
@Tag("integration")
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class PostLikesControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private PostLikesController postLikesController;


    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(postLikesController).build();
        namedParameterJdbcTemplate.getJdbcTemplate().execute("RUNSCRIPT FROM 'classpath:db/data.sql'");
    }

    @AfterEach
    void cleanUp() {
        namedParameterJdbcTemplate.getJdbcTemplate().execute("RUNSCRIPT FROM 'classpath:db/cleanup.sql'");
    }

    @Test
    void likePost() throws Exception {
        mockMvc.perform(post("/api/posts/{id}/likes", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(equalTo("6")));
    }


}

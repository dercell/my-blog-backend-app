package integration.dao;

import config.integration.PostIntegrationConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.model.PagePostResponse;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.PostService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PostIntegrationConfig.class)
class PostIntegrationTest {

    @Autowired
    private PostService postService;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    void setUp() {
        namedParameterJdbcTemplate.getJdbcTemplate().execute("RUNSCRIPT FROM 'classpath:db/data.sql'");
    }

    @AfterEach
    void cleanUp() {
        namedParameterJdbcTemplate.getJdbcTemplate().execute("RUNSCRIPT FROM 'classpath:db/cleanup.sql'");
    }

    static Stream<Arguments> searchData() {
        return Stream.of(
                Arguments.of("", 2),
                Arguments.of("#tag3", 1),
                Arguments.of("пост #tag2", 2)
        );
    }

    @ParameterizedTest
    @MethodSource("searchData")
    void getPosts(String search, Integer rowsCount) {
        PagePostResponse result = postService.getPosts(search, 1, 5);
        System.out.println(result.posts());

        assertEquals(rowsCount, result.posts().size());

    }

    @Test
    void getPostById() {
        Optional<Post> p = postService.getPostById(1L);
        assertEquals("Первый пост", p.map(Post::getTitle).orElse(null));
    }

    @Test
    void savePost() throws SQLException {
        Post newPost = Post.builder()
                .id(3L).title("Третий пост").text("Текст третьего")
                .tags(List.of("tag5")).likesCount(0).commentsCount(0).build();
        Optional<Post> savedPost = postService.savePost(newPost);

        assertEquals(newPost.getTitle(), savedPost.map(Post::getTitle).orElse(null));
    }

    @Test
    void updatePost() {
        Post p = Post.builder()
                .id(2L).title("Обновленный пост").text("Текст новый")
                .tags(List.of("tag5")).likesCount(0).commentsCount(2).build();
        Optional<Post> updatedPost = postService.updatePost(p, 2L);

        assertEquals(p.getTitle(), updatedPost.map(Post::getTitle).orElse(null));
    }

    @Test
    void deletePostById() throws IOException {
        postService.deletePostById(2L);

        Optional<Post> p = postService.getPostById(2L);
        assertTrue(p.isEmpty());

    }

    @Test
    void likePost() {
        Integer likes = postService.likePost(2L);
        assertEquals(13, likes);
    }

    @Test
    void uploadPostImage() throws IOException {
        MultipartFile file = new MockMultipartFile("image.png", "image.png", null, new byte[]{1, 2, 3, 4});

        postService.uploadPostImage(2L, file);
        Resource resource = postService.downloadPostImage(2L);
        assertArrayEquals(file.getBytes(), resource.getContentAsByteArray());
    }


}

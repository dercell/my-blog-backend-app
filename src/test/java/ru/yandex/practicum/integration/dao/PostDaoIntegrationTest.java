package ru.yandex.practicum.integration.dao;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.dao.PostDao;
import ru.yandex.practicum.dao.impl.H2PostDaoImpl;
import ru.yandex.practicum.model.PagePostResponse;
import ru.yandex.practicum.model.PostRequestDto;
import ru.yandex.practicum.model.PostResponseDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Tag("dao")
@Tag("integration")
@JdbcTest
@Import(H2PostDaoImpl.class)
class PostDaoIntegrationTest {

    @Autowired
    private PostDao postDao;

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
        PagePostResponse result = postDao.findAll(search, 1, 5);
        System.out.println(result.posts());

        assertEquals(rowsCount, result.posts().size());

    }

    @Test
    void getPostById() {
        Optional<PostResponseDto> p = postDao.findById(1L);
        assertEquals("Первый пост", p.map(PostResponseDto::getTitle).orElse(null));
    }

    @Test
    void savePost() {
        PostRequestDto newPostRequest = PostRequestDto.builder()
                .title("Третий пост").text("Текст третьего").tags(List.of("tag5", "tag6")).build();
        Long newId = postDao.save(newPostRequest);

        Optional<PostResponseDto> savedPost = postDao.findById(newId);

        assertEquals(newPostRequest.getTitle(), savedPost.map(PostResponseDto::getTitle).orElse(null));
    }

    @Test
    void updatePost() {
        PostRequestDto p = PostRequestDto.builder()
                .id(2L).title("Обновленный пост").text("Текст новый")
                .tags(List.of("tag5")).build();
        postDao.update(p, 2L);

        Optional<PostResponseDto> updatedPost = postDao.findById(2L);

        assertEquals(p.getTitle(), updatedPost.map(PostResponseDto::getTitle).orElse(null));
    }

    @Test
    void deletePostById() {
        postDao.delete(2L);

        Optional<PostResponseDto> p = postDao.findById(2L);
        assertTrue(p.isEmpty());

    }

    @Test
    void likePost() {
        postDao.incrementLike(2L);
        Optional<PostResponseDto> updatedPost = postDao.findById(2L);
        assertEquals(13, updatedPost.map(PostResponseDto::getLikesCount).orElse(null));
    }

    @Test
    void uploadPostImage() {
        String filename = "image.png";

        postDao.updateImage(2L, filename);
        String result = postDao.getFilenameByPostId(2L);
        assertEquals(filename, result);
    }

}

package unit.service;

import config.unit.PostUnitConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.dao.PostDao;
import ru.yandex.practicum.dao.PostImageStorage;
import ru.yandex.practicum.model.PagePostResponse;
import ru.yandex.practicum.model.PostCreateRequest;
import ru.yandex.practicum.model.PostResponse;
import ru.yandex.practicum.model.PostUpdateRequest;
import ru.yandex.practicum.service.PostService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PostUnitConfig.class)
@Tag("unit")
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostDao postDao;

    @Autowired
    private PostImageStorage postImageStorage;

    @Test
    void getPosts() {
        PagePostResponse ppr = new PagePostResponse(
                List.of(PostResponse.builder().id(1L).title("Post #1").text("Post text 1").tags(List.of("tag1")).likesCount(0).commentsCount(0).build(),
                        PostResponse.builder().id(2L).title("Post #2").text("Post text 2").tags(List.of("tag1", "tag2")).likesCount(10).commentsCount(5).build(),
                        PostResponse.builder().id(3L).title("Post #3").text("Post text 3").tags(List.of("tag2", "tag3")).likesCount(123).commentsCount(3).build()),
                false, false, 1
        );

        when(postDao.findAll("coffee #tag1", 1, 5)).thenReturn(ppr);

        PagePostResponse result = postService.getPosts("coffee #tag1", 1, 5);

        assertEquals(3, result.posts().size());

    }


    @Test
    void getPostById() {
        PostResponse p = PostResponse.builder().id(1L).title("Post #1").text("Post text 1").tags(List.of("tag1")).likesCount(0).commentsCount(0).build();
        when(postDao.findById(1L)).thenReturn(Optional.of(p));
        Optional<PostResponse> result = postService.getPostById(1L);

        assertEquals("Post #1", result.map(PostResponse::getTitle).orElse(""));

    }

    @Test
    void savePost() {
        PostCreateRequest p = PostCreateRequest.builder()
                .title("Post #2").text("Post text 2").tags(List.of("tag1", "tag2")).build();

        PostResponse response = PostResponse.builder()
                .title("Post #2").text("Post text 2").tags(List.of("tag1", "tag2")).build();

        when(postDao.save(p)).thenReturn(2L);
        when(postDao.findById(2L)).thenReturn(Optional.ofNullable(response));
        Optional<PostResponse> result = postService.savePost(p);

        assertEquals(p.getTitle(), result.map(PostResponse::getTitle).orElse(""));

    }

    @Test
    void updatePost() {
        PostUpdateRequest p = PostUpdateRequest.builder()
                .id(2L).title("Post #2").text("Post text 2").tags(List.of("tag1", "tag2")).build();

        PostResponse response = PostResponse.builder()
                .id(2L).title("Post #2").text("Post text 2").tags(List.of("tag1", "tag2")).build();

        when(postDao.findById(p.getId())).thenReturn(Optional.of(response));
        Optional<PostResponse> result = postService.updatePost(p, p.getId());

        assertEquals(p.getTitle(), result.map(PostResponse::getTitle).orElse(""));
        verify(postDao, times(1)).update(p, p.getId());
    }

    @Test
    void deletePostById() throws IOException {
        when(postDao.getFilenameByPostId(3L)).thenReturn("image.png");
        postService.deletePostById(3L);

        verify(postDao, times(1)).delete(3L);
        verify(postImageStorage, times(1)).delete("image.png");
    }

    @Test
    void likePost() {
        PostResponse p = PostResponse.builder().id(2L).title("Post #2").text("Post text 2").tags(List.of("tag1", "tag2")).likesCount(1).commentsCount(5).build();
        when(postDao.findById(2L)).thenReturn(Optional.of(p));

        Integer result = postService.likePost(2L);
        assertEquals(1, result);
        verify(postDao, times(1)).incrementLike(2L);

    }

    @Test
    void uploadPostImage() throws IOException {
        MultipartFile file = new MockMultipartFile("image.png", new byte[]{1, 2, 3});
        when(postImageStorage.upload(file)).thenReturn("image.png");

        postService.uploadPostImage(2L, file);
        verify(postDao, times(1)).updateImage(2L, "image.png");


    }

    @Test
    void downloadPostImage() throws IOException {
        Resource expectedFile = new ByteArrayResource(new byte[]{1, 2, 3});
        when(postDao.getFilenameByPostId(2L)).thenReturn("image.png");
        when(postImageStorage.download("image.png")).thenReturn(expectedFile);

        Resource result = postService.downloadPostImage(2L);

        assertArrayEquals(expectedFile.getContentAsByteArray(), result.getContentAsByteArray());
        verify(postDao, times(1)).getFilenameByPostId(2L);

    }


}

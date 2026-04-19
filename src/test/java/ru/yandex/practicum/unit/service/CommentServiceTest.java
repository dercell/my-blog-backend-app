package ru.yandex.practicum.unit.service;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.service.CommentService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("ru/yandex/practicum/my_blog_backend_app/unit")
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentDao commentDao;

    @Test
    void testFindAllByPostId() {
        when(commentDao.findAllByPostId(2L)).thenReturn(List.of(
                Comment.builder().id(3L).text("Comment 3").postId(2L).build(),
                Comment.builder().id(4L).text("Comment 4").postId(2L).build(),
                Comment.builder().id(5L).text("Comment 5").postId(2L).build()
        ));

        List<Comment> comments = commentService.findAllByPostId(2L);

        assertEquals(3, comments.size());
    }

    @Test
    void testGetById() {

        when(commentDao.getById(2L, 5L)).thenReturn(
                Optional.ofNullable(Comment.builder().id(5L).text("Comment 5").postId(2L).build()));
        Optional<Comment> c = commentService.getById(2L, 5L);
        assertEquals("Comment 5", c.map(Comment::getText).orElse(null));
    }

    @Test
    void testSaveComment() {

        Comment comment = Comment.builder().id(6L).text("Comment 6").postId(1L).build();
        when(commentDao.saveComment(1L, comment)).thenReturn(comment.getId());
        when(commentDao.getById(1L, 6L)).thenReturn(Optional.of(comment));

        Optional<Comment> savedComment = commentService.saveComment(1L, comment);

        assertEquals(comment, savedComment.orElse(null));
    }

    @Test
    void testUpdateComment() {
        Comment newComment = Comment.builder().id(6L).text("Comment 6").postId(1L).build();
        when(commentDao.getById(1L, 6L)).thenReturn(Optional.of(newComment));
        Optional<Comment> savedComment = commentService.updateComment(1L, 6L, newComment);

        assertEquals(newComment.getText(), savedComment.map(Comment::getText).orElse(null));
        verify(commentDao, times(1)).updateComment(1L, 6L, newComment);

    }

    @Test
    void testDeleteComment() {
        commentService.deleteComment(2L, 3L);

        verify(commentDao, times(1)).deleteComment(2L, 3L);
    }

    @Test
    void testDeleteAllPostComments() {
        commentService.deleteAllPostComments(1L);

        verify(commentDao, times(1)).deleteAllPostComments(1L);
    }

}

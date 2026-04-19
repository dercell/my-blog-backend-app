package ru.yandex.practicum.integration.dao;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.dao.impl.H2CommentDaoImpl;
import ru.yandex.practicum.model.Comment;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@Tag("dao")
@Tag("integration")
@Import(H2CommentDaoImpl.class)
class CommentDaoIntegrationTest {

    @Autowired
    private CommentDao commentDao;


    @Test
    void findAllByPostId() {
        List<Comment> commentList = commentDao.findAllByPostId(2L);
        assertEquals(1, commentList.size());
    }

    @Test
    void getById() {
        Optional<Comment> commentOptional = commentDao.getById(1L, 2L);

        assertEquals("Согласен!", commentOptional.map(Comment::getText).orElse(null));

    }

    @Test
    void saveComment() {
        Comment newComment = Comment.builder().text("Новый коммент").postId(1L).build();
        Long newId = commentDao.saveComment(1L, newComment);

        List<Comment> commentList = commentDao.findAllByPostId(1L);
        assertEquals(3, commentList.size());
        assertEquals(newComment.getText(), commentList.stream().filter(c -> c.getId().equals(newId)).map(Comment::getText)
                .findFirst().orElse(null));

    }

    @Test
    void updateComment() {
        Comment updateComment = Comment.builder().id(3L).text("Обвноленный коммент").postId(2L).build();
        commentDao.updateComment(2L, 3L, updateComment);

        Optional<Comment> updatedCommentOpt = commentDao.getById(2L, 3L);

        assertEquals(updateComment.getText(), updatedCommentOpt.map(Comment::getText).orElse(null));
    }

    @Test
    void deleteComment() {
        commentDao.deleteComment(1L, 1L);
        List<Comment> commentList = commentDao.findAllByPostId(1L);
        assertEquals(1, commentList.size());
    }

    @Test
    void deleteAllPostComments() {
        commentDao.deleteAllPostComments(1L);
        List<Comment> commentList = commentDao.findAllByPostId(1L);
        assertTrue(commentList.isEmpty());
    }


}

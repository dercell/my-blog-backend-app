package integration.dao;

import config.integration.CommentIntegrationConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.service.CommentService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CommentIntegrationConfig.class)
@Tag("integration")
@Tag("dao")
class CommentIntegrationTest {

    @Autowired
    private CommentService commentService;

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

    @Test
    void findAllByPostId() {
        List<Comment> commentList = commentService.findAllByPostId(1L);
        assertEquals(2, commentList.size());
    }

    @Test
    void getById() {
        Optional<Comment> commentOptional = commentService.getById(1L, 2L);

        assertEquals("Согласен!", commentOptional.map(Comment::getText).orElse(null));

    }

    @Test
    void saveComment() {
        Comment newComment = Comment.builder().text("Новый коммент").postId(1L).build();
        Optional<Comment> savedComment = commentService.saveComment(1L, newComment);

        List<Comment> commentList = commentService.findAllByPostId(1L);
        assertEquals(newComment.getText(), savedComment.map(Comment::getText).orElse(null));
        assertEquals(3, commentList.size());

    }

    @Test
    void updateComment() {
        Comment updateComment = Comment.builder().id(3L).text("Обвноленный коммент").postId(2L).build();
        Optional<Comment> updatedComment = commentService.updateComment(2L, 3L, updateComment);

        assertEquals(updateComment.getText(), updatedComment.map(Comment::getText).orElse(null));
    }

    @Test
    void deleteComment() {
        commentService.deleteComment(1L, 1L);
        List<Comment> commentList = commentService.findAllByPostId(1L);
        assertEquals(1, commentList.size());
    }

    @Test
    void deleteAllPostComments() {
        commentService.deleteAllPostComments(1L);
        List<Comment> commentList = commentService.findAllByPostId(1L);
        assertTrue(commentList.isEmpty());
    }


}

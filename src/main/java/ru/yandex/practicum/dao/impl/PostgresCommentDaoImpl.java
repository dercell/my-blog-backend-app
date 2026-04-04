package ru.yandex.practicum.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.util.mapper.CommentMapper;
import ru.yandex.practicum.util.mapper.PostMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class PostgresCommentDaoImpl implements CommentDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String SELECT_ALL_COMMENTS_BY_POST_ID_SQL = """
            select id, text, post_id from my_blog.comments where post_id = :post_id
            """;

    private static final String SELECT_COMMENT_BY_POST_ID_AND_ID = """
            select id, text, post_id from my_blog.comments where post_id = :post_id and id = :id
            """;

    private static final String INSERT_COMMENT_SQL = """
            insert into my_blog.comments(text, post_id)
            values(:text, :post_id) returning id;
            """;

    private static final String UPDATE_COMMENT_SQL = """
            update my_blog.comments
            set text = :text
            where post_id = :post_id and id = :id
            """;

    private static final String DELETE_COMMENT_SQL = """
            delete from my_blog.comments where post_id = :post_id and id = :id
            """;

    @Override
    public List<Comment> findAllByPostId(Long postId) {
        return namedParameterJdbcTemplate.query(SELECT_ALL_COMMENTS_BY_POST_ID_SQL,
                Map.of("post_id", postId), CommentMapper.commentRowMapper());
    }

    @Override
    public Optional<Comment> getById(Long postId, Long id) {

        try {
            Comment c = namedParameterJdbcTemplate.queryForObject(SELECT_COMMENT_BY_POST_ID_AND_ID,
                    Map.of("post_id", postId, "id", id),
                    CommentMapper.commentRowMapper());

            return Optional.ofNullable(c);
        } catch (DataAccessException dae) {
            log.error("Error in getById:", dae);
            return Optional.empty();
        }


    }

    @Override
    public Long saveComment(Long postId, Comment comment) {

        return namedParameterJdbcTemplate.queryForObject(INSERT_COMMENT_SQL,
                Map.of("text", comment.getText(), "post_id", postId),
                Long.class);
    }

    @Override
    public void updateComment(Long postId, Long id, Comment comment) {
        namedParameterJdbcTemplate.update(UPDATE_COMMENT_SQL,
                Map.of("text", comment.getText(),
                        "post_id", postId,
                        "id", id));
    }

    @Override
    public void deleteComment(Long postId, Long id) {
        namedParameterJdbcTemplate.update(DELETE_COMMENT_SQL, Map.of("post_id", postId, "id", id));
    }
}

package ru.yandex.practicum.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.util.mapper.CommentMapper;


import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class PostgresCommentDaoImpl implements CommentDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String POST_ID = "post_id";

    private static final String SELECT_ALL_COMMENTS_BY_POST_ID_SQL = """
            select id, text, post_id from my_blog.comments where post_id = :post_id
            """;

    private static final String SELECT_COMMENT_BY_POST_ID_AND_ID = """
            select id, text, post_id from my_blog.comments where post_id = :post_id and id = :id
            """;

    private static final String INSERT_COMMENT_SQL = """
            insert into my_blog.comments(text, post_id) values(:text, :post_id)
            """;

    private static final String UPDATE_COMMENT_SQL = """
            update my_blog.comments
            set text = :text
            where post_id = :post_id and id = :id
            """;

    private static final String DELETE_COMMENT_SQL = """
            delete from my_blog.comments where post_id = :post_id and id = :id
            """;

    private static final String DELETE_ALL_POST_COMMENTS_SQL = """
            delete from my_blog.comments where post_id = :post_id
            """;

    @Override
    public List<Comment> findAllByPostId(Long postId) {
        return namedParameterJdbcTemplate.query(SELECT_ALL_COMMENTS_BY_POST_ID_SQL,
                Map.of(POST_ID, postId), CommentMapper.commentRowMapper());
    }

    @Override
    public Optional<Comment> getById(Long postId, Long id) {

        try {
            Comment c = namedParameterJdbcTemplate.queryForObject(SELECT_COMMENT_BY_POST_ID_AND_ID,
                    Map.of(POST_ID, postId, "id", id),
                    CommentMapper.commentRowMapper());

            return Optional.ofNullable(c);
        } catch (DataAccessException dae) {
            log.error("Error in getById:", dae);
            return Optional.empty();
        }


    }

    @Override
    public Long saveComment(Long postId, Comment comment) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("text", comment.getText())
                .addValue(POST_ID, postId);

        namedParameterJdbcTemplate.update(INSERT_COMMENT_SQL, params, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();

    }

    @Override
    public void updateComment(Long postId, Long id, Comment comment) {
        namedParameterJdbcTemplate.update(UPDATE_COMMENT_SQL,
                Map.of("text", comment.getText(),
                        POST_ID, postId,
                        "id", id));
    }

    @Override
    public void deleteComment(Long postId, Long id) {
        namedParameterJdbcTemplate.update(DELETE_COMMENT_SQL, Map.of(POST_ID, postId, "id", id));
    }

    @Override
    public void deleteAllPostComments(Long postId) {
        namedParameterJdbcTemplate.update(DELETE_ALL_POST_COMMENTS_SQL, Map.of(POST_ID, postId));
    }
}

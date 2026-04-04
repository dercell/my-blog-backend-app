package ru.yandex.practicum.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.util.mapper.CommentMapper;

import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class PostgresCommentDaoImpl implements CommentDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String SELECT_ALL_COMMENTS_BY_POST_ID_SQL = """
            select id, text, post_id from my_blog.comments where post_id = :post_id
            """;

    @Override
    public List<Comment> findAllByPostId(Long postId) {
        return namedParameterJdbcTemplate.query(SELECT_ALL_COMMENTS_BY_POST_ID_SQL,
                Map.of("post_id", postId), CommentMapper.commentRowMapper());
    }
}

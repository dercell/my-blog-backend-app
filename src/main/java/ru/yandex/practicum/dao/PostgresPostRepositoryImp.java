package ru.yandex.practicum.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.util.mapper.PostMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Repository
public class PostgresPostRepositoryImp implements PostRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String ALL_POSTS_SQL = """
            select p.id, p.title, p.text, p.tags, p.likes_count, count(c.id) as comments_count
            from my_blog.posts p
                left join my_blog.comments c on p.id = c.post_id
            group by p.id, p.title, p.text, p.tags""";

    private static final String GET_POST_BY_ID_SQL =
            """
                    select p.id, p.title, p.text, p.tags, p.likes_count, count(c.id) as comments_count
                    from my_blog.posts p
                        left join my_blog.comments c on p.id = c.post_id
                        where p.id = :id
                    group by p.id, p.title, p.text, p.tags""";

    public PostgresPostRepositoryImp(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Post> findAll() {
        return namedParameterJdbcTemplate.query(ALL_POSTS_SQL, PostMapper.postRowMapper());
    }

    @Override
    public Optional<Post> findById(Long id) {
        try {
            Post p = namedParameterJdbcTemplate.queryForObject(GET_POST_BY_ID_SQL,
                    Map.of("id", id),
                    PostMapper.postRowMapper()
            );

            return Optional.ofNullable(p);
        } catch (DataAccessException dae) {
            log.error("Error in findById:", dae);
            return Optional.empty();
        }

    }

}

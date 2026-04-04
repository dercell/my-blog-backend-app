package ru.yandex.practicum.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.dao.PostDao;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.util.mapper.PostMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Repository
@AllArgsConstructor
public class PostgresPostDaoImp implements PostDao {
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

    private static final String INSERT_POST_SQL = """
            insert into my_blog.posts(title, text, tags)
            values(:title, :text, :tags) returning id
            """;

    private static final String UPDATE_POST_SQL = """
            update my_blog.posts
            set title = :title,
                text = :text,
                tags = :tags
            where id = :id
            """;

    private static final String UPDATE_INC_LIKE_SQL = """
            update my_blog.posts
            set likes_count = likes_count + 1
            where id = :id
            returning likes_count
            """;

    private static final String DELETE_POST_SQL = "delete from my_blog.posts where id = :id";

    private static final String UPDATE_POST_IMAGE_SQL = """
            update my_blog.posts
            set file_name = :filename
            where id = :id
            """;

    private static final String SELECT_FILENAME_BY_POST_ID_SQL = """
            select file_name from my_blog.posts where id = :id
            """;

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

    @Override
    public Long save(Post post) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", post.getTitle())
                .addValue("text", post.getText())
                .addValue("tags", post.getTags().toArray(new String[0]));

        return namedParameterJdbcTemplate.queryForObject(INSERT_POST_SQL, params, Long.class);
    }

    @Override
    public void update(Post post, Long id) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", post.getTitle())
                .addValue("text", post.getText())
                .addValue("tags", post.getTags().toArray(new String[0]))
                .addValue("id", id);

        namedParameterJdbcTemplate.update(UPDATE_POST_SQL, params);
    }

    @Override
    public void delete(Long id) {
        namedParameterJdbcTemplate.update(DELETE_POST_SQL, Map.of("id", id));
    }

    @Override
    public Integer incrementLike(Long id) {
        return namedParameterJdbcTemplate.queryForObject(UPDATE_INC_LIKE_SQL, Map.of("id", id), Integer.class);
    }

    @Override
    public void updateImage(Long id, String savedFilename) {
        namedParameterJdbcTemplate.update(UPDATE_POST_IMAGE_SQL, Map.of("filename", savedFilename, "id", id));
    }

    @Override
    public String getFilenameByPostId(Long id) {
        try{
            return namedParameterJdbcTemplate.queryForObject(SELECT_FILENAME_BY_POST_ID_SQL, Map.of("id", id), String.class);
        } catch (EmptyResultDataAccessException ex){
            log.error("Empty result", ex);
            return null;
        }
    }

}

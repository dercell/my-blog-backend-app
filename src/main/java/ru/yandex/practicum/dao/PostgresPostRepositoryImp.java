package ru.yandex.practicum.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Post;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class PostgresPostRepositoryImp implements PostRepository {
    private final JdbcTemplate jdbcTemplate;

    public PostgresPostRepositoryImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Post> findAll() {
        return jdbcTemplate.query("""
                        select p.id, p.title, p.text, p.tags, count(c.id) as comments_count
                        from my_blog.posts p
                            left join my_blog.comments c on p.id = c.post_id
                        group by p.id, p.title, p.text, p.tags""",
                (rs, rowNum) -> Post.builder()
                        .id(rs.getLong("id"))
                        .title(rs.getString("title"))
                        .text(rs.getString("text"))
                        .tags(Arrays.asList((String[]) rs.getArray("tags").getArray()))
                        .commentsCount(rs.getInt("comments_count"))
                        .build());
    }

}

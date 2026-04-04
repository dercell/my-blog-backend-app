package ru.yandex.practicum.util.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.Post;

import java.util.Arrays;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostMapper {

    public static RowMapper<Post> postRowMapper() {
        return (rs, rowNum) -> Post.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .text(rs.getString("text"))
                .tags(Arrays.asList((String[]) rs.getArray("tags").getArray()))
                .likesCount(rs.getInt("likes_count"))
                .commentsCount(rs.getInt("comments_count"))
                .build();
    }

}

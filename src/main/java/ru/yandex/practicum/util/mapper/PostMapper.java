package ru.yandex.practicum.util.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.PostResponse;

import java.util.Arrays;
import java.util.Objects;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostMapper {

    public static RowMapper<PostResponse> postRowMapper() {
        return (rs, rowNum) -> PostResponse.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .text(rs.getString("text"))
                .tags(Arrays.stream((Object[]) rs.getArray("tags").getArray()).filter(Objects::nonNull).map(String::valueOf).toList())
                .likesCount(rs.getInt("likes_count"))
                .commentsCount(rs.getInt("comments_count"))
                .build();
    }

}

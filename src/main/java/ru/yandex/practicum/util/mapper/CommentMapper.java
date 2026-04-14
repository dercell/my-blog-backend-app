package ru.yandex.practicum.util.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.model.Comment;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {

    public static RowMapper<Comment> commentRowMapper(){
        return (rs, rowNum) -> Comment
                .builder()
                .id(rs.getLong("id"))
                .text(rs.getString("text"))
                .postId(rs.getLong("post_id"))
                .build();
    }

}

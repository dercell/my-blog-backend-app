package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.PagePostResponse;
import ru.yandex.practicum.model.Post;

import java.sql.SQLException;
import java.util.Optional;

public interface PostDao {

    PagePostResponse findAll(String search, int pageNumber, int pageSize);

    Optional<Post> findById(Long id);

    Long save(Post post) throws SQLException;

    void update(Post post, Long id);

    void delete(Long id);

    Integer incrementLike(Long id);

    void updateImage(Long id, String savedFilename);

    String getFilenameByPostId(Long id);
}

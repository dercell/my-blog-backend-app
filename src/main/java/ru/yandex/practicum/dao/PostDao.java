package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostDao {

    List<Post> findAll();

    Optional<Post> findById(Long id);

    Long save(Post post);

    void update(Post post, Long id);

    void delete(Long id);

    Integer incrementLike(Long id);

    void updateImage(Long id, String savedFilename);

    String getFilenameByPostId(Long id);
}

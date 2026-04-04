package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    List<Post> findAll();

    Optional<Post> findById(Long id);

    Long save(Post post);

}

package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentDao {

    List<Comment> findAllByPostId(Long postId);

    Optional<Comment> getById(Long postId, Long id);

    Long saveComment(Long postId, Comment comment);

    void updateComment(Long postId, Comment comment);
}

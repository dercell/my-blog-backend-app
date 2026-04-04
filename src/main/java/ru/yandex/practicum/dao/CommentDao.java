package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Comment;

import java.util.List;

public interface CommentDao {

    List<Comment> findAllByPostId(Long postId);

}

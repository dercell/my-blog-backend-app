package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.model.Comment;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentDao commentDao;

    public List<Comment> findAllByPostId(Long postId){
        return commentDao.findAllByPostId(postId);
    }

    public Optional<Comment> getById(Long postId, Long id) {
        return commentDao.getById(postId, id);
    }

    public Optional<Comment> saveComment(Long postId, Comment comment) {
        Long commentId = commentDao.saveComment(postId, comment);
        return commentDao.getById(postId, commentId);
    }
}

package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.model.Comment;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentDao commentDao;

    public List<Comment> findAllByPostId(Long postId){
        return commentDao.findAllByPostId(postId);
    }

}

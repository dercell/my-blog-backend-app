package ru.yandex.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{id}/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public List<Comment> getAll(@PathVariable("id") Long postId){
        return commentService.findAllByPostId(postId);
    }

}

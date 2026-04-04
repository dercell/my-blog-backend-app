package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public List<Comment> findAllByPostId(@PathVariable("postId") Long postId) {
        return commentService.findAllByPostId(postId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getById(@PathVariable("postId") Long postId, @PathVariable("id") Long id) {
        return commentService
                .getById(postId, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Comment> saveComment(@PathVariable("postId") Long postId,
                                               @RequestBody @Valid Comment comment) {
        return commentService
                .saveComment(postId, comment)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping
    public ResponseEntity<Comment> updateComment(@PathVariable("postId") Long postId,
                                                 @RequestBody @Valid Comment comment){
        return commentService
                .updateComment(postId, comment)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());

    }

}

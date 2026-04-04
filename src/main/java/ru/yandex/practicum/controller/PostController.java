package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.PostService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private PostService postService;


    @GetMapping("/")
    public List<Post> getPosts() {
        return postService.getPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable("id") Long id) {
        return postService
                .getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Post> savePost(@RequestBody @Valid Post post) {
        return postService
                .save(post)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

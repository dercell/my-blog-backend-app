package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.BlogService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private BlogService blogService;

    @GetMapping("/")
    public List<Post> getPosts() {
        return blogService.getPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable("id") Long id) {
        return blogService
                .getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Post> savePost(@RequestBody @Valid Post post) {
        return blogService
                .savePost(post)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@RequestBody @Valid Post post, @PathVariable("id") Long id) {
        return blogService
                .updatePost(post, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable("id") Long id) {
        blogService.deletePostById(id);
    }

}

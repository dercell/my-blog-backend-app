package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.PagePostResponse;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.PostService;

import java.io.IOException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private PostService postService;

    @GetMapping
    public PagePostResponse getPosts(@RequestParam("search") String search,
                                     @RequestParam("pageNumber") int pageNum,
                                     @RequestParam("pageSize") int pageSize) {

        return postService.getPosts(search, pageNum, pageSize);
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
                .savePost(post)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@RequestBody @Valid Post post, @PathVariable("id") Long id) {
        return postService
                .updatePost(post, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable("id") Long id) throws IOException {
        postService.deletePostById(id);
    }

}

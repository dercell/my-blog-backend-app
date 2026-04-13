package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.PagePostResponse;
import ru.yandex.practicum.model.PostCreateRequest;
import ru.yandex.practicum.model.PostResponse;
import ru.yandex.practicum.model.PostUpdateRequest;
import ru.yandex.practicum.service.PostService;

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
    public ResponseEntity<PostResponse> getPostById(@PathVariable("id") Long id) {
        return postService
                .getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PostResponse> savePost(@RequestBody @Valid PostCreateRequest postCreateRequest) {
        return postService
                .savePost(postCreateRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@RequestBody @Valid PostUpdateRequest postUpdateRequest, @PathVariable("id") Long id) {
        return postService
                .updatePost(postUpdateRequest, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable("id") Long id) {
        postService.deletePostById(id);
    }

}

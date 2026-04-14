package ru.yandex.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.service.PostService;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/posts/{id}")
public class PostLikesController {

    private PostService postService;

    @PostMapping("/likes")
    public Integer likePost(@PathVariable("id") Long id) {
        return postService.likePost(id);
    }

}

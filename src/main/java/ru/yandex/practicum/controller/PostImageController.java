package ru.yandex.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.service.PostService;


@RestController
@AllArgsConstructor
@RequestMapping("/api/posts/{id}/image")
public class PostImageController {

    private PostService postService;

    @PutMapping
    public void uploadFile(@PathVariable("id") Long id, @RequestParam("image") MultipartFile file) {
        postService.uploadPostImage(id, file);
    }

    @GetMapping
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Long id) {
        Resource postImage = postService.downloadPostImage(id);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(postImage);
    }
}

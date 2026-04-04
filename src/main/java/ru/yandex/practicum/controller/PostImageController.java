package ru.yandex.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.service.PostService;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/posts/{id}/images")
public class PostImageController {

    private PostService postService;

    @PutMapping
    public void uploadFile(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) throws IOException {
        postService.uploadPostImage(id, file);
    }

    @GetMapping
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Long id) throws IOException {
        Resource postImage = postService.downloadPostImage(id);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(postImage);
    }
}

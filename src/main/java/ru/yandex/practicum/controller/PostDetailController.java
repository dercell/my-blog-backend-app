package ru.yandex.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.service.BlogService;

import java.io.IOException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/posts/{id}")
public class PostDetailController {

    private BlogService blogService;

    @PostMapping("/likes")
    public Integer likePost(@PathVariable("id") Long id) {
        return blogService.likePost(id);
    }

    @PutMapping("/image")
    public void uploadFile(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) throws IOException {
        blogService.uploadPostImage(id, file);
    }

    @GetMapping("/image")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Long id) throws IOException {
        Resource postImage = blogService.downloadPostImage(id);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(postImage);
    }


}

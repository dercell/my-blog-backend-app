package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.PostRepository;
import ru.yandex.practicum.model.Post;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService {

    private PostRepository postRepository;

    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public Optional<Post> save(Post post) {
        Long newPostId = postRepository.save(post);
        return postRepository.findById(newPostId);
    }

    public Optional<Post> update(Post post, Long id) {
        postRepository.update(post, id);
        return postRepository.findById(id);
    }

}

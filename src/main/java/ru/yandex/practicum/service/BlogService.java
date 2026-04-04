package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BlogService {
    private PostRepository postRepository;

    public List<Post> getPosts() {
        return postRepository.getPosts();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.getPostById(id);
    }

    public Optional<Post> savePost(Post post) {
        return postRepository.save(post);
    }

    public Optional<Post> updatePost(Post post, Long id) {
        return postRepository.update(post, id);
    }

    public void deletePostById(Long id){
        //commentRepository.deleteByPostId(id);
        postRepository.deleteById(id);
    }


}

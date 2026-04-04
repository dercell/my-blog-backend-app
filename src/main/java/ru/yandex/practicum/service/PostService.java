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

    public List<Post> getPosts(){
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long id){
        return postRepository.findById(id);
    }

    public void save(Post post){
        postRepository.save(post);
    }

}

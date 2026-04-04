package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.PostRepository;
import ru.yandex.practicum.model.Post;

import java.awt.print.Pageable;
import java.util.List;

@Service
@AllArgsConstructor
public class PostService {

    private PostRepository postRepository;

    public List<Post> getPosts(){
        return postRepository.findAll();
    }

}

package ru.yandex.practicum.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.dao.PostDao;
import ru.yandex.practicum.model.Post;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PostRepository {

    private PostDao postDao;

    public List<Post> getPosts() {
        return postDao.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        return postDao.findById(id);
    }

    public Optional<Post> save(Post post) {
        Long newPostId = postDao.save(post);
        return postDao.findById(newPostId);
    }

    public Optional<Post> update(Post post, Long id) {
        postDao.update(post, id);
        return postDao.findById(id);
    }

    public void deleteById(Long id){
        postDao.delete(id);
    }

}

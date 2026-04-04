package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.dao.PostDao;
import ru.yandex.practicum.dao.PostImageStorage;
import ru.yandex.practicum.model.Post;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService {

    private final PostDao postDao;
    private final PostImageStorage postImageStorage;

    public List<Post> getPosts() {
        return postDao.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        return postDao.findById(id);
    }

    public Optional<Post> savePost(Post post) {
        Long newPostId = postDao.save(post);
        return postDao.findById(newPostId);
    }

    public Optional<Post> updatePost(Post post, Long id) {
        postDao.update(post, id);
        return postDao.findById(id);
    }

    public void deletePostById(Long id) throws IOException {
        String filename = postDao.getFilenameByPostId(id);
        //commentRepository.deleteByPostId(id);
        postDao.delete(id);
        if (filename != null){
            postImageStorage.delete(filename);
        }
    }

    public Integer likePost(Long id) {
        return postDao.incrementLike(id);
    }

    public void uploadPostImage(Long id, MultipartFile file) throws IOException {
        String savedFilename = postImageStorage.upload(file);
        postDao.updateImage(id, savedFilename);
    }

    public Resource downloadPostImage(Long id) throws IOException {
        String filename = postDao.getFilenameByPostId(id);
        return postImageStorage.download(filename);
    }


}

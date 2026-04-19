package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.dao.PostDao;
import ru.yandex.practicum.dao.PostImageStorage;
import ru.yandex.practicum.model.PagePostResponse;
import ru.yandex.practicum.model.PostResponseDto;
import ru.yandex.practicum.model.PostRequestDto;
import ru.yandex.practicum.util.exceptions.StorageException;


import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PostService {

    private final PostDao postDao;
    private final PostImageStorage postImageStorage;

    public PagePostResponse getPosts(String search, int pageNumber, int pageSize) {
        return postDao.findAll(search, pageNumber, pageSize);
    }

    public Optional<PostResponseDto> getPostById(Long id) {
        return postDao.findById(id);
    }

    public Optional<PostResponseDto> savePost(PostRequestDto postRequestDto) {
        Long newPostId = postDao.save(postRequestDto);
        return postDao.findById(newPostId);
    }

    public Optional<PostResponseDto> updatePost(PostRequestDto postRequestDto, Long id) {
        postDao.update(postRequestDto, id);
        return postDao.findById(id);
    }

    @Transactional
    public void deletePostById(Long id) {
        try {
            String filename = postDao.getFilenameByPostId(id);
            postDao.delete(id);
            if (filename != null) {
                postImageStorage.delete(filename);
            }
        } catch (IOException ioe) {
            log.error("Error in deletePostById: {}", ioe.getMessage(), ioe);
            throw new StorageException("Error in deletePostById:" + ioe.getMessage());
        }

    }

    public Integer likePost(Long id) {
        postDao.incrementLike(id);
        return postDao.findById(id).map(PostResponseDto::getLikesCount).orElse(null);
    }

    public void uploadPostImage(Long id, MultipartFile file) {
        try {
            String savedFilename = postImageStorage.upload(file);
            postDao.updateImage(id, savedFilename);
        } catch (IOException ioe) {
            log.error("Error in uploadPostImage: {}", ioe.getMessage(), ioe);
            throw new StorageException("Error in uploadPostImage:" + ioe.getMessage());
        }
    }

    public Resource downloadPostImage(Long id) {
        try {
            String filename = postDao.getFilenameByPostId(id);
            return postImageStorage.download(filename);
        } catch (IOException ioe) {
            log.error("Error in downloadPostImage: {}", ioe.getMessage(), ioe);
            throw new StorageException("Error in downloadPostImage:" + ioe.getMessage());
        }
    }


}

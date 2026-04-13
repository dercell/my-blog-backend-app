package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.PagePostResponse;
import ru.yandex.practicum.model.PostCreateRequest;
import ru.yandex.practicum.model.PostResponse;
import ru.yandex.practicum.model.PostUpdateRequest;

import java.util.Optional;

public interface PostDao {

    PagePostResponse findAll(String search, int pageNumber, int pageSize);

    Optional<PostResponse> findById(Long id);

    Long save(PostCreateRequest postCreateRequest);

    void update(PostUpdateRequest postUpdateResponse, Long id);

    void delete(Long id);

    void incrementLike(Long id);

    void updateImage(Long id, String savedFilename);

    String getFilenameByPostId(Long id);
}

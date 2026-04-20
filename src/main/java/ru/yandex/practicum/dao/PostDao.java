package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.PagePostResponse;
import ru.yandex.practicum.model.PostResponseDto;
import ru.yandex.practicum.model.PostRequestDto;

import java.util.Optional;

public interface PostDao {

    PagePostResponse findAll(String search, int pageNumber, int pageSize);

    Optional<PostResponseDto> findById(Long id);

    Long save(PostRequestDto postRequestDto);

    void update(PostRequestDto postUpdateResponse, Long id);

    void delete(Long id);

    void incrementLike(Long id);

    void updateImage(Long id, String savedFilename);

    String getFilenameByPostId(Long id);
}

package ru.yandex.practicum.model;

import java.util.List;

public record PagePostResponse(
        List<PostResponseDto> posts,
        boolean hasPrev,
        boolean hasNext,
        int lastPage
) {
}

package ru.yandex.practicum.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Comment {

    @JsonProperty
    private Long id;

    @JsonProperty
    private String text;

    @JsonProperty
    private Long postId;
}

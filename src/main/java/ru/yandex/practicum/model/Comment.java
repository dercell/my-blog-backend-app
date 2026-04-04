package ru.yandex.practicum.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @JsonProperty
    private Long id;

    @NotBlank
    @JsonProperty
    private String text;

    @NotBlank
    @JsonProperty
    private Long postId;
}

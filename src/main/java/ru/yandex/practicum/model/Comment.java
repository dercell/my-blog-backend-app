package ru.yandex.practicum.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @JsonProperty
    private Long postId;
}

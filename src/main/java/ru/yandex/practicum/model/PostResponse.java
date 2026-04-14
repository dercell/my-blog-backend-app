package ru.yandex.practicum.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    @JsonProperty
    private Long id;

    @NotBlank(message = "Не может быть пустым")
    @JsonProperty
    private String title;

    @NotBlank(message = "Не может быть пустым")
    @JsonProperty
    private String text;

    @NotNull(message = "Необходимо указать")
    @NotEmpty(message = "Не может быть пустым")
    @JsonProperty
    private List<String> tags;

    @JsonProperty
    private Integer likesCount;

    @JsonProperty
    private Integer commentsCount;

}

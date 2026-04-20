package ru.yandex.practicum.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {

    @JsonProperty
    private Long id;

    @NotBlank(message = "Не может быть пустым")
    @JsonProperty
    private String title;

    @NotBlank(message = "Не может быть пустым")
    @JsonProperty
    private String text;

    @NotEmpty(message = "Не может быть пустым")
    @JsonProperty
    private List<String> tags;
}

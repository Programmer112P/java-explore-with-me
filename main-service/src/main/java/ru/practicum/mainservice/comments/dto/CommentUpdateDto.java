package ru.practicum.mainservice.comments.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentUpdateDto {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 500)
    private String message;
}

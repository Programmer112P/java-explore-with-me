package ru.practicum.mainservice.categories.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewCategoryDto {

    @NotBlank
    @Size(max = 50)
    private String name;
}

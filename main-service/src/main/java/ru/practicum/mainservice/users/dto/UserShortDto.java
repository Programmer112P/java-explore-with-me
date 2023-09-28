package ru.practicum.mainservice.users.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserShortDto {

    private Long id;

    private String name;
}

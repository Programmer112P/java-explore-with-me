package ru.practicum.mainservice.exception.errorhandler;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiError {

    private String status;

    private String reason;

    private String message;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}

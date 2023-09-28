package ru.practicum.mainservice.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewEventDto {

    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Long category;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @Valid
    @NotNull
    private Location location;

    @Builder.Default
    private Boolean paid = false;

    @Builder.Default
    private Integer participantLimit = 0;

    @Builder.Default
    private Boolean requestModeration = true;

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    @JsonSetter("paid")
    public void setPaid(Boolean paid) {
        if (paid != null) {
            this.paid = paid;
        }
    }

    @JsonSetter("participantLimit")
    public void setParticipantLimit(Integer participantLimit) {
        if (participantLimit != null) {
            this.participantLimit = participantLimit;
        }
    }

    @JsonSetter("requestModeration")
    public void setRequestModeration(Boolean requestModeration) {
        if (requestModeration != null) {
            this.requestModeration = requestModeration;
        }
    }
}

package ru.practicum.mainservice.events.dto;

import lombok.*;
import ru.practicum.mainservice.requests.model.Status;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;

    private Status status;
}

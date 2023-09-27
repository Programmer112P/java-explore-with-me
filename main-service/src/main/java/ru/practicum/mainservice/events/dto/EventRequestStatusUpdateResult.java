package ru.practicum.mainservice.events.dto;

import lombok.*;
import ru.practicum.mainservice.requests.dto.ParticipationRequestDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventRequestStatusUpdateResult {

    private List<ParticipationRequestDto> confirmedRequests;

    private List<ParticipationRequestDto> rejectedRequests;
}

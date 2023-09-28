package ru.practicum.mainservice.requests.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.mainservice.requests.dto.ParticipationRequestDto;
import ru.practicum.mainservice.requests.model.Request;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestMapper {

    public ParticipationRequestDto getDtoFromModel(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .status(request.getStatus())
                .build();
    }

    public List<ParticipationRequestDto> getDtoListFromModelList(List<Request> requests) {
        return requests.stream().map(this::getDtoFromModel).collect(Collectors.toList());
    }

}

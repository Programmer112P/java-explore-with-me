package ru.practicum.mainservice.requests.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.requests.dto.ParticipationRequestDto;
import ru.practicum.mainservice.requests.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@Validated
public class RequestController {

    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable Long userId,
                                                 @RequestParam Long eventId) {
        log.info("RequestController createRequest: request to create request to event {}", eventId);
        ParticipationRequestDto response = requestService.createRequest(userId, eventId);
        log.info("RequestController createRequest: completed request to create request to event {}", eventId);
        return response;
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId,
                                                 @PathVariable Long requestId) {
        log.info("RequestController cancelRequest: request to cancel request");
        ParticipationRequestDto response = requestService.cancelRequest(userId, requestId);
        log.info("RequestController cancelRequest: completed request to cancel request");
        return response;
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId) {
        log.info("RequestController getRequests: request to get requests for user {}", userId);
        List<ParticipationRequestDto> response = requestService.getRequests(userId);
        log.info("RequestController getRequests: completed request to get requests for user {}", userId);
        return response;
    }
}

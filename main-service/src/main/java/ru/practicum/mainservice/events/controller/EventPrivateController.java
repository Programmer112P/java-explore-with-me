package ru.practicum.mainservice.events.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.events.dto.*;
import ru.practicum.mainservice.events.service.EventService;
import ru.practicum.mainservice.requests.dto.ParticipationRequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@Validated
public class EventPrivateController {

    private final EventService eventService;

    @Autowired
    public EventPrivateController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId, @RequestBody @Valid NewEventDto newEventDto) {
        log.info("EventPrivateController createEvent: request to create event");
        EventFullDto response = eventService.createEvent(userId, newEventDto);
        log.info("EventPrivateController createEvent: completed request to create event");
        return response;
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        log.info("EventPrivateController updateEvent: request to update event {}", eventId);
        EventFullDto response = eventService.userUpdateEvent(userId, eventId, updateEventUserRequest);
        log.info("EventPrivateController updateEvent: completed request to update event {}", eventId);
        return response;
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getAllUserEvents(@PathVariable Long userId,
                                                @RequestParam(name = "from", required = false, defaultValue = "0") Long offset,
                                                @RequestParam(name = "size", required = false, defaultValue = "10") Integer limit) {
        log.info("EventPrivateController getAllUserEvents: request to get all events from user {}", userId);
        List<EventShortDto> response = eventService.getAllUserEvents(userId, offset, limit);
        log.info("EventPrivateController getAllUserEvents: completed request to get all events from user {}", userId);
        return response;
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventById(@PathVariable Long userId,
                                     @PathVariable Long eventId) {
        log.info("EventPrivateController getEventById: request to get event with id {}", eventId);
        EventFullDto response = eventService.getEventById(userId, eventId);
        log.info("EventPrivateController getEventById: completed request to get event with id {}", eventId);
        return response;
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventRequest(@PathVariable Long userId,
                                                             @PathVariable Long eventId,
                                                             @RequestBody @Valid EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("EventPrivateController updateEventRequest: request to update event Request");
        EventRequestStatusUpdateResult response = eventService.updateEventRequest(userId, eventId, eventRequestStatusUpdateRequest);
        log.info("EventPrivateController updateEventRequest: completed request to update event Request");
        return response;
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequests(@PathVariable Long userId,
                                                          @PathVariable Long eventId) {
        log.info("EventPrivateController getEventRequests: request to get requests for event");
        List<ParticipationRequestDto> response = eventService.getEventRequests(userId, eventId);
        log.info("EventPrivateController getEventRequests: completed request to get requests for event");
        return response;
    }
}

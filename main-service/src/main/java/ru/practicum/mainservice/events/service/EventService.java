package ru.practicum.mainservice.events.service;

import ru.practicum.mainservice.events.dto.*;
import ru.practicum.mainservice.events.model.State;
import ru.practicum.mainservice.requests.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    EventFullDto userUpdateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<EventShortDto> getAllUserEvents(Long userId, Long offset, Integer limit);

    EventFullDto getEventById(Long userId, Long eventId);

    EventFullDto adminUpdateEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventFullDto> getAdminEvents(List<Long> users, List<State> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Long offset, Integer limit);

    List<EventShortDto> getPublishedEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, SortOrder sortOrder, Long offset, Integer limit);

    EventFullDto getPublishedEventById(Long id, String userIp);

    EventRequestStatusUpdateResult updateEventRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId);
}

package ru.practicum.mainservice.events.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.mainservice.categories.dto.CategoryDto;
import ru.practicum.mainservice.categories.model.Category;
import ru.practicum.mainservice.events.dto.EventFullDto;
import ru.practicum.mainservice.events.dto.EventShortDto;
import ru.practicum.mainservice.events.dto.Location;
import ru.practicum.mainservice.events.dto.NewEventDto;
import ru.practicum.mainservice.events.model.Event;
import ru.practicum.mainservice.events.model.State;
import ru.practicum.mainservice.users.dto.UserShortDto;
import ru.practicum.mainservice.users.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventMapper {

    public Event getModelFromNewEventDto(User initiator, Category category, NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .lat(newEventDto.getLocation().getLat())
                .lon(newEventDto.getLocation().getLon())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .createdOn(LocalDateTime.now())
                .initiator(initiator)
                .state(State.PENDING)
                .confirmedRequests(0L)
                .views(0L)
                .build();
    }

    public EventShortDto getEventShortDtoFromModel(Event event) {
        CategoryDto category = CategoryDto.builder()
                .id(event.getCategory().getId())
                .name(event.getCategory().getName())
                .build();
        UserShortDto initiator = UserShortDto.builder()
                .id(event.getInitiator().getId())
                .name(event.getInitiator().getName())
                .build();

        return EventShortDto.builder()
                .id(event.getId())
                .description(event.getDescription())
                .annotation(event.getAnnotation())
                .category(category)
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(initiator)
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public List<EventShortDto> getEventShortListDtoFromModelList(List<Event> events) {
        return events.stream().map(this::getEventShortDtoFromModel).collect(Collectors.toList());
    }

    public EventFullDto getEventFullDtoFromModel(Event event) {
        CategoryDto category = CategoryDto.builder()
                .id(event.getCategory().getId())
                .name(event.getCategory().getName())
                .build();

        UserShortDto initiator = UserShortDto.builder()
                .id(event.getInitiator().getId())
                .name(event.getInitiator().getName())
                .build();

        Location location = Location.builder()
                .lat(event.getLat())
                .lon(event.getLon())
                .build();

        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(category)
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(initiator)
                .location(location)
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public List<EventFullDto> getEventFullDtoListFromModelList(List<Event> events) {
        return events.stream().map(this::getEventFullDtoFromModel).collect(Collectors.toList());
    }
}

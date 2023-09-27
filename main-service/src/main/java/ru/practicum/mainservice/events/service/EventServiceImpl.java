package ru.practicum.mainservice.events.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.categories.model.Category;
import ru.practicum.mainservice.categories.repository.CategoryRepository;
import ru.practicum.mainservice.events.dto.*;
import ru.practicum.mainservice.events.mapper.EventMapper;
import ru.practicum.mainservice.events.model.Event;
import ru.practicum.mainservice.events.model.QEvent;
import ru.practicum.mainservice.events.model.State;
import ru.practicum.mainservice.events.model.View;
import ru.practicum.mainservice.events.repository.EventRepository;
import ru.practicum.mainservice.events.repository.ViewRepository;
import ru.practicum.mainservice.exception.BadRequestException;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.pagination.OffsetBasedPageRequest;
import ru.practicum.mainservice.requests.dto.ParticipationRequestDto;
import ru.practicum.mainservice.requests.mapper.RequestMapper;
import ru.practicum.mainservice.requests.model.Request;
import ru.practicum.mainservice.requests.model.Status;
import ru.practicum.mainservice.requests.repository.RequestRepository;
import ru.practicum.mainservice.users.model.User;
import ru.practicum.mainservice.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final ViewRepository viewRepository;

    @Autowired
    public EventServiceImpl(UserRepository userRepository, EventMapper eventMapper, CategoryRepository categoryRepository, EventRepository eventRepository,
                            RequestRepository requestRepository, RequestMapper requestMapper, ViewRepository viewRepository) {
        this.userRepository = userRepository;
        this.eventMapper = eventMapper;
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
        this.requestRepository = requestRepository;
        this.requestMapper = requestMapper;
        this.viewRepository = viewRepository;
    }

    @Override
    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        User initiator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Category category = categoryRepository
                .findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2L))) {
            throw new BadRequestException("Invalid event date");
        }
        Event event = eventMapper.getModelFromNewEventDto(initiator, category, newEventDto);
        Event created = eventRepository.save(event);
        return eventMapper.getEventFullDtoFromModel(created);
    }

    @Override
    @Transactional
    public EventFullDto userUpdateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (!userId.equals(event.getInitiator().getId())) {
            throw new NotFoundException("Event not available for user");
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Only pending or canceled events can be changed");
        }
        updateFieldsFromUser(updateEventUserRequest, event);
        Event updated = eventRepository.save(event);
        return eventMapper.getEventFullDtoFromModel(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getAllUserEvents(Long userId, Long offset, Integer limit) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        OffsetBasedPageRequest pageRequest = new OffsetBasedPageRequest(offset, limit);
        List<Event> events = eventRepository.findAllByInitiator(user, pageRequest);
        return eventMapper.getEventShortListDtoFromModelList(events);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventById(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new NotFoundException("Event not available for user");
        }
        return eventMapper.getEventFullDtoFromModel(event);
    }

    @Override
    @Transactional
    public EventFullDto adminUpdateEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        validateAdminUpdate(event, updateEventAdminRequest);
        updateFieldsFromAdmin(event, updateEventAdminRequest);
        Event updated = eventRepository.save(event);
        return eventMapper.getEventFullDtoFromModel(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getAdminEvents(List<Long> users, List<State> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Long offset, Integer limit) {
        if (rangeStart != null && rangeEnd != null && rangeEnd.isBefore(rangeStart)) {
            throw new BadRequestException("End date before start date");
        }
        OffsetBasedPageRequest offsetBasedPageRequest = new OffsetBasedPageRequest(offset, limit);
        List<Event> events = eventRepository.findAll(
                getUsersIdCriteria(users)
                        .and(getStatesCriteria(states))
                        .and(getCategoriesIdCriteria(categories))
                        .and(getRangeStartCriteria(rangeStart))
                        .and(getRangeEndCriteria(rangeEnd)),
                offsetBasedPageRequest
        ).stream().collect(Collectors.toList());
        return eventMapper.getEventFullDtoListFromModelList(events);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getPublishedEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, SortOrder sortOrder, Long offset, Integer limit) {
        if (rangeEnd != null && rangeStart != null && rangeEnd.isBefore(rangeStart)) {
            throw new BadRequestException("End date before start date");
        }
        Sort sort;
        if (sortOrder.equals(SortOrder.EVENT_DATE)) {
            sort = Sort.by(Sort.Direction.ASC, "eventDate");
        } else {
            sort = Sort.by(Sort.Direction.ASC, "views");
        }
        OffsetBasedPageRequest offsetBasedPageRequest = new OffsetBasedPageRequest(offset, limit, sort);
        List<Event> events = eventRepository.findAll(
                getAnnotationCriteria(text)
                        .and(getDescriptionCriteria(text).or(getAnnotationCriteria(text)))
                        .and(getCategoriesIdCriteria(categories))
                        .and(getPublishedStateCriteria())
                        .and(getPaidCriteria(paid))
                        .and(getTimePeriodCriteria(rangeStart, rangeEnd))
                        .and(getOnlyAvailableCriteria(onlyAvailable)),
                offsetBasedPageRequest
        ).stream().collect(Collectors.toList());
        return eventMapper.getEventShortListDtoFromModelList(events);
    }

    @Override
    @Transactional
    public EventFullDto getPublishedEventById(Long id, String userIp) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Event not found"));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("Event not published");
        }
        if (!viewRepository.existsByEventAndUserIp(event, userIp)) {
            event.setViews(event.getViews() + 1);
            View view = getView(event, userIp);
            viewRepository.save(view);
        }
        Event saved = eventRepository.save(event);
        return eventMapper.getEventFullDtoFromModel(saved);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateEventRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new NotFoundException("Event not available for user");
        }
        validateRequests(event, eventRequestStatusUpdateRequest.getStatus());
        List<Request> requests = requestRepository.findAllById(eventRequestStatusUpdateRequest.getRequestIds());
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            return EventRequestStatusUpdateResult.builder()
                    .confirmedRequests(requestMapper.getDtoListFromModelList(requests))
                    .rejectedRequests(Collections.emptyList())
                    .build();
        }
        Status status = eventRequestStatusUpdateRequest.getStatus();
        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();
        for (Request request : requests) {
            updateRequest(request, event, status, confirmedRequests, rejectedRequests);
        }
        requestRepository.saveAll(requests);
        eventRepository.save(event);
        return EventRequestStatusUpdateResult.builder()
                .rejectedRequests(requestMapper.getDtoListFromModelList(rejectedRequests))
                .confirmedRequests(requestMapper.getDtoListFromModelList(confirmedRequests))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new NotFoundException("Event not available for user");
        }
        List<Request> requests = requestRepository.findAllByEvent(event);
        return requestMapper.getDtoListFromModelList(requests);
    }

    private void updateRequest(Request request, Event event, Status status, List<Request> confirmedRequests, List<Request> rejectedRequests) {
        if (!request.getStatus().equals(Status.PENDING)) {
            throw new ConflictException("Request must have status PENDING");
        }
        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            request.setStatus(Status.REJECTED);
            rejectedRequests.add(request);
        } else {
            if (status.equals(Status.CONFIRMED)) {
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                confirmedRequests.add(request);
            } else {
                rejectedRequests.add(request);
            }
            request.setStatus(status);
        }
    }

    private View getView(Event event, String userIp) {
        return View.builder()
                .userIp(userIp)
                .event(event)
                .build();
    }

    private void validateRequests(Event event, Status status) {
        if (event.getParticipantLimit() != 0 && status.equals(Status.CONFIRMED) && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("Participants limit already reached");
        }
    }

    private BooleanExpression getAnnotationCriteria(String text) {
        if (text == null) {
            return Expressions.asBoolean(true).isTrue();
        }
        return QEvent.event.annotation.containsIgnoreCase(text);
    }

    private BooleanExpression getDescriptionCriteria(String text) {
        if (text == null) {
            return Expressions.asBoolean(true).isTrue();
        }
        return QEvent.event.description.contains(text);
    }

    private BooleanExpression getPublishedStateCriteria() {
        return QEvent.event.state.eq(State.PUBLISHED);
    }

    private BooleanExpression getTimePeriodCriteria(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeEnd == null || rangeStart == null) {
            return QEvent.event.eventDate.after(LocalDateTime.now());
        }
        return QEvent.event.eventDate.between(rangeStart, rangeEnd);
    }

    private BooleanExpression getPaidCriteria(Boolean paid) {
        if (paid == null) {
            return Expressions.asBoolean(true).isTrue();
        }
        return QEvent.event.paid.eq(paid);
    }

    private BooleanExpression getOnlyAvailableCriteria(Boolean onlyAvailable) {
        if (!onlyAvailable) {
            return Expressions.asBoolean(true).isTrue();
        }
        return QEvent.event.confirmedRequests.lt(QEvent.event.participantLimit);
    }


    private BooleanExpression getUsersIdCriteria(List<Long> usersId) {
        if (usersId.isEmpty()) {
            return Expressions.asBoolean(true).isTrue();
        }
        return QEvent.event.initiator.id.in(usersId);
    }

    private BooleanExpression getStatesCriteria(List<State> states) {
        if (states.isEmpty()) {
            return Expressions.asBoolean(true).isTrue();
        }
        return QEvent.event.state.in(states);
    }

    private BooleanExpression getCategoriesIdCriteria(List<Long> categories) {
        if (categories.isEmpty()) {
            return Expressions.asBoolean(true).isTrue();
        }
        return QEvent.event.category.id.in(categories);
    }

    private BooleanExpression getRangeStartCriteria(LocalDateTime rangeStart) {
        if (rangeStart == null) {
            return Expressions.asBoolean(true).isTrue();
        }
        return QEvent.event.eventDate.after(rangeStart);
    }

    private BooleanExpression getRangeEndCriteria(LocalDateTime rangeEnd) {
        if (rangeEnd == null) {
            return Expressions.asBoolean(true).isTrue();
        }
        return QEvent.event.eventDate.before(rangeEnd);
    }

    private void updateFieldsFromAdmin(Event event, UpdateEventAdminRequest update) {
        if (update.getCategory() != null) {
            Category category = categoryRepository.findById(update.getCategory()).orElseThrow(() -> new NotFoundException("Category not found"));
            event.setCategory(category);
        }
        if (update.getEventDate() != null) {
            LocalDateTime newEventDate = update.getEventDate();
            if (newEventDate.isBefore(LocalDateTime.now().plusHours(2L))) {
                throw new BadRequestException("Invalid event date");
            }
            event.setEventDate(newEventDate);
        }
        if (update.getLocation() != null) {
            event.setLon(update.getLocation().getLon());
            event.setLat(update.getLocation().getLat());
        }
        event.setAnnotation(update.getAnnotation() == null ? event.getAnnotation() : update.getAnnotation());
        event.setDescription(update.getDescription() == null ? event.getDescription() : update.getDescription());
        event.setPaid(update.getPaid() == null ? event.getPaid() : update.getPaid());
        event.setParticipantLimit(update.getParticipantLimit() == null ? event.getParticipantLimit() : update.getParticipantLimit());
        event.setRequestModeration(update.getRequestModeration() == null ? event.getRequestModeration() : update.getRequestModeration());
        event.setTitle(update.getTitle() == null ? event.getTitle() : update.getTitle());
        if (update.getStateAction() != null) {
            event.setState(update.getStateAction().equals(AdminStateAction.PUBLISH_EVENT) ? State.PUBLISHED : State.CANCELED);
        }
        if (event.getState().equals(State.PUBLISHED)) {
            event.setPublishedOn(LocalDateTime.now());
        }
    }

    private void validateAdminUpdate(Event event, UpdateEventAdminRequest updateEventAdminRequest) {
        if (event.getPublishedOn() != null && !event.getEventDate().isAfter(event.getPublishedOn().plusHours(1))) {
            throw new ConflictException("Event is not available to update");
        }
        if (updateEventAdminRequest.getStateAction() != null && updateEventAdminRequest.getStateAction().equals(AdminStateAction.PUBLISH_EVENT)
                && !event.getState().equals(State.PENDING)) {
            throw new ConflictException("Event is in state " + event.getState());
        }
        if (updateEventAdminRequest.getStateAction() != null && updateEventAdminRequest.getStateAction().equals(AdminStateAction.REJECT_EVENT)
                && event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Event can't be rejected");
        }
    }

    private void updateFieldsFromUser(UpdateEventUserRequest update, Event event) {
        if (update.getCategory() != null) {
            Category category = categoryRepository.findById(update.getCategory()).orElseThrow(() -> new NotFoundException("Category not found"));
            event.setCategory(category);
        }
        if (update.getEventDate() != null) {
            LocalDateTime newEventDate = update.getEventDate();
            if (newEventDate.isBefore(LocalDateTime.now().plusHours(2L))) {
                throw new BadRequestException("Invalid event date");
            }
            event.setEventDate(newEventDate);
        }
        if (update.getLocation() != null) {
            event.setLon(update.getLocation().getLon());
            event.setLat(update.getLocation().getLat());
        }
        event.setAnnotation(update.getAnnotation() == null ? event.getAnnotation() : update.getAnnotation());
        event.setDescription(update.getDescription() == null ? event.getDescription() : update.getDescription());
        event.setPaid(update.getPaid() == null ? event.getPaid() : update.getPaid());
        event.setParticipantLimit(update.getParticipantLimit() == null ? event.getParticipantLimit() : update.getParticipantLimit());
        event.setRequestModeration(update.getRequestModeration() == null ? event.getRequestModeration() : update.getRequestModeration());
        event.setTitle(update.getTitle() == null ? event.getTitle() : update.getTitle());
        if (update.getStateAction() != null) {
            event.setState(update.getStateAction().equals(UserStateAction.CANCEL_REVIEW) ? State.CANCELED : State.PENDING);
        }
    }
}

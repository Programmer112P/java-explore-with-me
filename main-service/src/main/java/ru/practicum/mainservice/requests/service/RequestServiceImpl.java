package ru.practicum.mainservice.requests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.events.model.Event;
import ru.practicum.mainservice.events.model.State;
import ru.practicum.mainservice.events.repository.EventRepository;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.requests.dto.ParticipationRequestDto;
import ru.practicum.mainservice.requests.mapper.RequestMapper;
import ru.practicum.mainservice.requests.model.Request;
import ru.practicum.mainservice.requests.model.Status;
import ru.practicum.mainservice.requests.repository.RequestRepository;
import ru.practicum.mainservice.users.model.User;
import ru.practicum.mainservice.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, RequestMapper requestMapper, UserRepository userRepository, EventRepository eventRepository) {
        this.requestRepository = requestRepository;
        this.requestMapper = requestMapper;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        validateRequest(user, event);
        Request request = getRequest(event, user);
        if (request.getStatus().equals(Status.CONFIRMED)) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }
        Request created = requestRepository.save(request);
        return requestMapper.getDtoFromModel(created);
    }


    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequests(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        List<Request> requests = requestRepository.findAllByRequester(user);
        return requestMapper.getDtoListFromModelList(requests);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Request not found"));
        if (!Objects.equals(request.getRequester().getId(), userId)) {
            throw new NotFoundException("Request is not available");
        }
        request.setStatus(Status.CANCELED);
        Request cancelled = requestRepository.save(request);
        return requestMapper.getDtoFromModel(cancelled);
    }

    private void validateRequest(User user, Event event) {
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Event not published");
        }
        if (user.getId().equals(event.getInitiator().getId())) {
            throw new ConflictException("User can't take part in his own event");
        }
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("Event has reached participants limit");
        }
        if (requestRepository.existsByEventAndRequester(event, user)) {
            throw new ConflictException("Request already exists");
        }
    }

    private Request getRequest(Event event, User user) {
        Boolean isRequestModeration = event.getRequestModeration();
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .requester(user)
                .event(event)
                .status(isRequestModeration ? Status.PENDING : Status.CONFIRMED)
                .build();
        if (event.getParticipantLimit() == 0) {
            request.setStatus(Status.CONFIRMED);
        }
        return request;
    }
}

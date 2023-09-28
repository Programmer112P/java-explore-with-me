package ru.practicum.mainservice.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.events.model.Event;
import ru.practicum.mainservice.requests.model.Request;
import ru.practicum.mainservice.users.model.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Boolean existsByEventAndRequester(Event event, User user);

    List<Request> findAllByRequester(User user);

    List<Request> findAllByEvent(Event event);
}

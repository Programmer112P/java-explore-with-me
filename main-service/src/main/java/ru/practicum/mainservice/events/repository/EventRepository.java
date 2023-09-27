package ru.practicum.mainservice.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.mainservice.events.model.Event;
import ru.practicum.mainservice.pagination.OffsetBasedPageRequest;
import ru.practicum.mainservice.users.model.User;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    List<Event> findAllByInitiator(User initiator, OffsetBasedPageRequest pageRequest);
}

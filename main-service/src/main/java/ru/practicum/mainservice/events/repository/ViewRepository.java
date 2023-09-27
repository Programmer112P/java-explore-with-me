package ru.practicum.mainservice.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.events.model.Event;
import ru.practicum.mainservice.events.model.View;

@Repository
public interface ViewRepository extends JpaRepository<View, Long> {

    Boolean existsByEventAndUserIp(Event event, String userIp);
}

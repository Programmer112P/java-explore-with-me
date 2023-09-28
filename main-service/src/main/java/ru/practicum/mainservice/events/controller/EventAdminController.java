package ru.practicum.mainservice.events.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.events.dto.EventFullDto;
import ru.practicum.mainservice.events.dto.UpdateEventAdminRequest;
import ru.practicum.mainservice.events.model.State;
import ru.practicum.mainservice.events.service.EventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
@Validated
public class EventAdminController {

    private final EventService eventService;

    @Autowired
    public EventAdminController(EventService eventService) {
        this.eventService = eventService;
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("EventAdminController updateEvent: request to update event");
        EventFullDto response = eventService.adminUpdateEvent(eventId, updateEventAdminRequest);
        log.info("EventAdminController updateEvent: completed request to update event");
        return response;
    }

    @GetMapping("/events")
    public List<EventFullDto> getEvents(@RequestParam(required = false, defaultValue = "") List<Long> users,
                                        @RequestParam(required = false, defaultValue = "") List<State> states,
                                        @RequestParam(required = false, defaultValue = "") List<Long> categories,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                        @RequestParam(name = "from", required = false, defaultValue = "0") Long offset,
                                        @RequestParam(name = "size", required = false, defaultValue = "10") Integer limit) {
        log.info("EventAdminController getEvents: request to get events from admin");
        List<EventFullDto> response = eventService.getAdminEvents(users, states, categories, rangeStart, rangeEnd, offset, limit);
        log.info("EventAdminController getEvents: completed request to get events from admin");
        return response;
    }
}

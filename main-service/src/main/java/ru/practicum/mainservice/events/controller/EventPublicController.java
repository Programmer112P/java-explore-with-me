package ru.practicum.mainservice.events.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.events.dto.EventFullDto;
import ru.practicum.mainservice.events.dto.EventShortDto;
import ru.practicum.mainservice.events.dto.SortOrder;
import ru.practicum.mainservice.events.service.EventService;
import ru.practicum.mainservice.events.service.StatsService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/events")
@Validated
public class EventPublicController {

    private final EventService eventService;
    private final StatsService statsService;

    @Autowired
    public EventPublicController(EventService eventService, StatsService statsService) {
        this.eventService = eventService;
        this.statsService = statsService;
    }

    @GetMapping
    public List<EventShortDto> getPublishedEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false, defaultValue = "") List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false, defaultValue = "EVENT_DATE") SortOrder sort,
            @RequestParam(name = "from", required = false, defaultValue = "0") Long offset,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer limit,
            HttpServletRequest request) {
        log.info("EventPublicController getPublishedEvents: request to get events");
        List<EventShortDto> response = eventService.getPublishedEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, offset, limit);
        String userIp = request.getRemoteAddr();
        String path = request.getRequestURI();
        statsService.postStatRequest(userIp, path);
        log.info("EventPublicController getPublishedEvents: completed request to get events");
        return response;
    }

    @GetMapping("/{id}")
    public EventFullDto getPublishedEventById(@PathVariable Long id, HttpServletRequest request) {
        log.info("EventPublicController getPublishedEventById: request to get event with id {}", id);
        String userIp = request.getRemoteAddr();
        String path = request.getRequestURI();
        EventFullDto response = eventService.getPublishedEventById(id, userIp);
        statsService.postStatRequest(userIp, path);
        log.info("EventPublicController getPublishedEventById: completed request to get event with id {}", id);
        return response;
    }

}

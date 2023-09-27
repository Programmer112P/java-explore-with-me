package ru.practicum.statserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statdto.HitDto;
import ru.practicum.statdto.ViewStatsDto;
import ru.practicum.statserver.service.StatServerService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@Validated
public class StatServerController {

    private final StatServerService statServerService;

    @Autowired
    public StatServerController(StatServerService statServerService) {
        this.statServerService = statServerService;
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(
            @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false, defaultValue = "") List<String> uris,
            @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.info("StatServerController getStats: get request for stats");
        List<ViewStatsDto> response = statServerService.getStats(start, end, uris, unique);
        log.info("StatServerController getStats: get request for stats completed");
        return response;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void hit(@RequestBody @Valid HitDto hitDto) {
        log.info("StatServerController getStats: post request hit {}", hitDto);
        statServerService.hit(hitDto);
        log.info("StatServerController getStats: created hit {}", hitDto);
    }
}

package ru.practicum.statserver.service;

import ru.practicum.statdto.HitDto;
import ru.practicum.statdto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatServerService {
    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

    void hit(HitDto hitDto);
}

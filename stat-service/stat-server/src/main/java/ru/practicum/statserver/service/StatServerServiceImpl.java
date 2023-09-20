package ru.practicum.statserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statdto.HitDto;
import ru.practicum.statdto.ViewStatsDto;
import ru.practicum.statserver.exception.BadInputException;
import ru.practicum.statserver.mapper.HitMapper;
import ru.practicum.statserver.model.Hit;
import ru.practicum.statserver.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatServerServiceImpl implements StatServerService {

    private final HitRepository hitRepository;
    private final HitMapper hitMapper;

    @Autowired
    public StatServerServiceImpl(HitRepository hitRepository, HitMapper hitMapper) {
        this.hitRepository = hitRepository;
        this.hitMapper = hitMapper;
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start.isAfter(end)) {
            throw new BadInputException("Get stats request: start date after end date");
        }
        if (unique && !uris.isEmpty()) {
            return hitRepository.countSelectedUriUniqueIpHits(uris, start, end);
        } else if (unique) {
            return hitRepository.countAllUriUniqueIpHits(start, end);
        } else if (!uris.isEmpty()) {
            return hitRepository.countSelectedUriAllIpHits(uris, start, end);
        } else {
            return hitRepository.countAllUriAllIpHits(start, end);
        }
    }

    @Override
    @Transactional
    public void hit(HitDto hitDto) {
        Hit hit = hitMapper.getHitFromDto(hitDto);
        hitRepository.save(hit);
    }
}

package ru.practicum.statserver.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.statdto.HitDto;
import ru.practicum.statserver.model.Hit;

@Component
public class HitMapper {

    public Hit getHitFromDto(HitDto hitDto) {
        return Hit.builder()
                .app(hitDto.getApp())
                .timestamp(hitDto.getTimestamp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .build();
    }
}

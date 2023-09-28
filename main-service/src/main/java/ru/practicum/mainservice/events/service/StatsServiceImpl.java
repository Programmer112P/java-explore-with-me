package ru.practicum.mainservice.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.statclient.StatClient;
import ru.practicum.statdto.HitDto;

import java.time.LocalDateTime;

@Service
public class StatsServiceImpl implements StatsService {

    @Value("${app.name}")
    private String appName;

    private final StatClient statClient;

    @Autowired
    public StatsServiceImpl(StatClient statClient) {
        this.statClient = statClient;
    }

    @Override
    public void postStatRequest(String userIp, String path) {
        HitDto hitDto = getHitDto(userIp, path);
        statClient.postStatRequest(hitDto);
    }

    private HitDto getHitDto(String userIp, String path) {
        return HitDto.builder()
                .app(appName)
                .ip(userIp)
                .uri(path)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

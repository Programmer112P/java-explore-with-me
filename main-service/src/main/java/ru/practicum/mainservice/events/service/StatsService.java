package ru.practicum.mainservice.events.service;

public interface StatsService {
    void postStatRequest(String userIp, String path);
}

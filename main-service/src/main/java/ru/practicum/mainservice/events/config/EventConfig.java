package ru.practicum.mainservice.events.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.statclient.StatClient;

@Configuration
public class EventConfig {

    @Autowired
    RestTemplateBuilder builder;

    @Value("${stat-server.url}")
    private String serverUrl;

    @Bean
    public StatClient statClient() {
        return new StatClient(serverUrl, builder);
    }
}

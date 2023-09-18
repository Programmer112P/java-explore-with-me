package ru.practicum.statserver.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hit")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Hit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String app;

    private String uri;

    private String ip;

    @Column(name = "hit_time")
    private LocalDateTime timestamp;
}

package ru.practicum.mainservice.requests.model;

import lombok.*;
import ru.practicum.mainservice.events.model.Event;
import ru.practicum.mainservice.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "request")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User requester;

    private Status status;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime created;
}

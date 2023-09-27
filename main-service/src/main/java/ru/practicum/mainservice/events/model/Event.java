package ru.practicum.mainservice.events.model;

import lombok.*;
import ru.practicum.mainservice.categories.model.Category;
import ru.practicum.mainservice.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String annotation;

    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @Column(name = "confirmed_requests")
    private Long confirmedRequests;

    @Column(name = "created_on", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdOn;

    @Column(name = "event_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User initiator;

    private Double lat;

    private Double lon;

    private Boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "published_on", columnDefinition = "TIMESTAMP")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private State state;

    private String title;

    private Long views;
}

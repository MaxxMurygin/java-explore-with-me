package ru.practicum.ewm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import ru.practicum.ewm.model.enums.EventState;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String annotation;
    private String description;
    @NotBlank
    private String title;
    @NotNull
    @Column(name = "category_id")
    private Long categoryId;
    @NotNull
    @Column(name = "initiator_id")
    private Long initiatorId;
    @Column(name = "confirmed_requests")
    private Long confirmedRequests;
    private Long views;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @NotNull
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @NotNull
    private Boolean paid;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private EventState state;
    @NotNull
    @Range(min = -90, max =  90, message = "Долгота в евклидовом пространстве находится в диапазоне [-90:90]")
    @Column(name = "location_lat")
    private Float locationLat;
    @NotNull
    @Range(min = -180, max =  180, message = "Широта в евклидовом пространстве находится в диапазоне [-180:180]")
    @Column(name = "location_lon")
    private Float locationLon;
}

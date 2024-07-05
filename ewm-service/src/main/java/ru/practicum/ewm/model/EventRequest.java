package ru.practicum.ewm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.model.enums.EventRequestStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "event_id")
    private Long eventId;
    @NotNull
    @Column(name = "requester_id")
    private Long requesterId;
    @NotNull
    @Column(name = "created_on")
    private LocalDateTime created;
    @NotNull
    @Enumerated
    private EventRequestStatus status;
}

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
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;
    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "requester_id", referencedColumnName = "id")
    private User requester;
    @NotNull
    @Column(name = "created_on")
    private LocalDateTime created;
    @NotNull
    @Enumerated
    private EventRequestStatus status;
}

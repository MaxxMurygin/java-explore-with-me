package ru.practicum.ewm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "compilations_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "compilation_id")
    private Long compilationId;
    @NotNull
    @Column(name = "event_id")
    private Long eventId;
}

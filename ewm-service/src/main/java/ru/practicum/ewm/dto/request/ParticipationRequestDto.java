package ru.practicum.ewm.dto.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.model.enums.EventRequestStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ParticipationRequestDto {
    private LocalDateTime created;
    private Long eventId;
    private Long id;
    private Long requesterId;
    private EventRequestStatus status;
}

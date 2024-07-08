package ru.practicum.ewm.dto.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.model.enums.EventRequestStatus;

@Data
@Builder
public class ParticipationRequestDto {
    private String created;
    private Long event;
    private Long id;
    private Long requester;
    private EventRequestStatus status;
}

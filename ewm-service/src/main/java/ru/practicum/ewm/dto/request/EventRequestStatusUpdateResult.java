package ru.practicum.ewm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}

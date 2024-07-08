package ru.practicum.ewm.dto.request;

import lombok.Data;
import ru.practicum.ewm.model.enums.EventRequestStatus;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {
    List<Long> requestsIds;
    EventRequestStatus status;
}

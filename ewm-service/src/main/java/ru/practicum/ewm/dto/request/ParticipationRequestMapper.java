package ru.practicum.ewm.dto.request;

import ru.practicum.ewm.model.EventRequest;
import ru.practicum.ewm.model.enums.EventRequestStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParticipationRequestMapper {
    public static ParticipationRequestDto toDto(EventRequest request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .requesterId(request.getRequester().getId())
                .eventId(request.getEvent().getId())
                .created(request.getCreated())
                .status(request.getStatus())
                .build();
    }

    public static EventRequestStatusUpdateResult toResult(List<EventRequest> requests) {
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        List<EventRequest> confirmed = new ArrayList<>();
        List<EventRequest> rejected = new ArrayList<>();

        for (EventRequest er: requests) {
            if (er.getStatus().equals(EventRequestStatus.CONFIRMED)) {
                confirmed.add(er);
            } else if (er.getStatus().equals(EventRequestStatus.REJECTED)) {
                rejected.add(er);
            }
        }
        result.setConfirmedRequests(confirmed.stream()
                .map(ParticipationRequestMapper::toDto)
                .collect(Collectors.toList()));
        result.setRejectedRequests(rejected.stream()
                .map(ParticipationRequestMapper::toDto)
                .collect(Collectors.toList()));

        return result;
    }
}

package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.EventRequest;

import java.util.List;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    EventRequest findByRequesterIdAndEventId(Long requesterId, Long eventId);

    List<EventRequest> findAllByRequesterId(Long requesterId);

    List<EventRequest> findAllByEventId(Long eventId);
}

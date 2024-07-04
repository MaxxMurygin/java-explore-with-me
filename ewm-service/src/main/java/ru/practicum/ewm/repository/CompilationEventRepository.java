package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.CompilationEvent;

import java.util.List;

public interface CompilationEventRepository extends JpaRepository<CompilationEvent, Long> {
    List<Long>  findByEventId(Long eventId);

    List<Long> findByCompilationId(Long compilationId);
}

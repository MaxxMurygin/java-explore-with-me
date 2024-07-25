package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    List<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    List<Event> findAllByCategoryId(Long categoryId);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM (VALUES (:ids)) as t(id) " +
            "JOIN events e on t.id = e.id")
    List<Event> findByIdInSortAsPassed(@Param("ids") List<Long> eventsIds, Pageable pageable);
}

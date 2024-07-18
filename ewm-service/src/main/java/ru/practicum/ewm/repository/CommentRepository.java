package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.CommentCounter;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByEventId(Long eventId, Pageable pageable);

    @Query("SELECT new ru.practicum.ewm.model.CommentCounter(c.eventId, COUNT(c.eventId)) " +
            "FROM Comment c " +
            "GROUP BY c.eventId " +
            "ORDER BY COUNT(c.eventId) DESC")
    List<CommentCounter> findMostDiscussedEvents();
}

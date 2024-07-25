package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.Complaint;
import ru.practicum.ewm.model.enums.ComplaintStatus;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findAllByStatusIn(ComplaintStatus[] statuses, Pageable pageable);
}

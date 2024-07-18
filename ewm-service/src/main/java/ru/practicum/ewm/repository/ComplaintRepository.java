package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.Complaint;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
}

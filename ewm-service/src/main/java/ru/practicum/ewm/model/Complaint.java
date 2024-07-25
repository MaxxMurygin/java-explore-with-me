package ru.practicum.ewm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.model.enums.ComplaintStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "complaints")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 2, max = 1000, message = "Содержание должно быть в диапазоне 2-1000 символов")
    private String text;
    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "comment_id", referencedColumnName = "id")
    private Comment comment;
    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "complainant_id", referencedColumnName = "id")
    private User complainant;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "reviewed_on")
    private LocalDateTime reviewedOn;
    @Enumerated(EnumType.STRING)
    private ComplaintStatus status;
}

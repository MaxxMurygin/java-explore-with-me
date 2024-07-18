package ru.practicum.ewm.dto.complaint;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.model.enums.ComplainStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class ComplaintDto {
    @NotNull
    private Long id;
    @NotNull
    private String text;
    @NotNull
    private Long commentId;
    @NotNull
    private Long complainantId  ;
    @NotNull
    private String created;
    @NotNull
    private ComplainStatus status;
}

package ru.practicum.ewm.dto.complaint;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.model.enums.ComplaintStatus;

import javax.validation.constraints.NotNull;

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
    private ComplaintStatus status;
}

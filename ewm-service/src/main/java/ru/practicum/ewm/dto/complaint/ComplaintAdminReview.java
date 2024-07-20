package ru.practicum.ewm.dto.complaint;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.model.enums.AdminCommentAction;
import ru.practicum.ewm.model.enums.ComplaintStatus;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ComplaintAdminReview {
    @NotNull
    private AdminCommentAction action;
    @NotNull
    private ComplaintStatus complaintStatus;
    private String changedCommentText;
}

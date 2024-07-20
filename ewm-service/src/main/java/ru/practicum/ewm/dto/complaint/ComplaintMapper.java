package ru.practicum.ewm.dto.complaint;

import ru.practicum.ewm.common.EwmDateFormatter;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.Complaint;
import ru.practicum.ewm.model.User;

import java.time.format.DateTimeFormatter;

public class ComplaintMapper {
    private static final DateTimeFormatter formatter = EwmDateFormatter.getFormatter();

    public static Complaint fromRequest(Comment comment, User complainant, ComplaintDtoRequest complaint) {

        return Complaint.builder()
                .text(complaint.getText())
                .comment(comment)
                .complainant(complainant)
                .build();
    }

    public static ComplaintDto toComplaintDto(Complaint complaint) {

        return ComplaintDto.builder()
                .id(complaint.getId())
                .text(complaint.getText())
                .created(complaint.getCreatedOn().format(formatter))
                .commentId(complaint.getComment().getId())
                .complainantId(complaint.getComplainant().getId())
                .status(complaint.getStatus())
                .build();
    }
}

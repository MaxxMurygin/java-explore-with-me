package ru.practicum.ewm.dto.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.ewm.model.enums.EventStateAdminAction;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateEventAdminRequest extends UpdateEventRequest {
    private EventStateAdminAction stateAction;
}

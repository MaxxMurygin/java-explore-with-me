package ru.practicum.ewm.dto.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.ewm.model.enums.EventStateUserAction;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateEventUserRequest extends UpdateEventRequest{
    private EventStateUserAction stateAction;
}

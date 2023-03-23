package ru.practicum.private_access.events.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDtoInputUpdate extends EventDtoInput {
    private StateAction stateAction;

    public enum StateAction {
        SEND_TO_REVIEW,
        CANCEL_REVIEW
    }
}

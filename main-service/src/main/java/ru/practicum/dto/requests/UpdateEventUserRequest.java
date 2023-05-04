package ru.practicum.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.domain.ActionState;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequest {

    private String annotation;

    private long category;

    private String description;

    private String eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private ActionState stateAction;

    private String title;

    @Data
    @AllArgsConstructor
    public static class Location {

        private float lat;

        private float lon;
    }
}

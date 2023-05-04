package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.domain.EventState;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {

    private long id;

    @NotNull
    @NotBlank
    private String annotation;

    private String description;

    @NotNull
    private CategoryDto category;

    private int confirmedRequests;

    private LocalDateTime createdOn;

    @NotNull
    private UserShortDto initiator;

    @NotNull
    private Location location;

    @NotNull
    private boolean paid;

    private int participantLimit;

    private LocalDateTime publishedOn;

    private boolean requestModeration;

    private EventState state;

    private String eventDate;

    @NotNull
    private String title;

    private int views;

    @Data
    @AllArgsConstructor
    public static class CategoryDto {

        private long id;

        private String name;
    }

    @Data
    @AllArgsConstructor
    public static class UserShortDto {

        private long id;

        private String name;
    }

    @Data
    @AllArgsConstructor
    public static class Location {

        private float lat;

        private float lon;
    }
}

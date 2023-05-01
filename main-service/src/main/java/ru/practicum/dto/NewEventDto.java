package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {

    @NotNull
    @NotBlank
    private String annotation;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    private long category;

    @NotNull
    private Location location;

    @NotNull
    private boolean paid;

    private int participantLimit;

    private boolean requestModeration;

    @NotNull
    private String eventDate;

    @NotNull
    private String title;

    @Data
    @AllArgsConstructor
    public static class Location {

        private long id;

        private float lat;

        private float lon;
    }
}

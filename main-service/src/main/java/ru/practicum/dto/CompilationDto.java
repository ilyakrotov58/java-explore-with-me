package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {

    private long id;

    @NotNull
    private boolean pinned;

    @NotBlank
    @NotNull
    private String title;

    private List<EventShortDto> events;

    @Data
    @AllArgsConstructor
    public static class EventShortDto {

        private long id;

        @NotNull
        @NotBlank
        private String annotation;

        @NotNull
        private CategoryDto category;

        private int confirmedRequests;

        private String eventDate;

        @NotNull
        private UserShortDto initiator;

        @NotNull
        private boolean paid;

        @NotNull
        @NotBlank
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
    }
}

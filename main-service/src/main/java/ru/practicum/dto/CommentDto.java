package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private long id;

    @NotNull
    private EventShortDto event;

    @NotNull
    private User user;

    @NotEmpty
    @NotBlank
    private String text;

    @Data
    @AllArgsConstructor
    public static class EventShortDto {

        private long id;

        @NotNull
        @NotBlank
        private String annotation;

        @NotNull
        private CategoryDto category;


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

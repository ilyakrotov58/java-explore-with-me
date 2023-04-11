package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HitDto {

    @NotNull
    @NotBlank
    private String app;

    @NotNull
    @NotBlank
    private String uri;

    @NotNull
    @NotBlank
    private String ip;

    @NotNull
    @NotBlank
    private String timestamp;
}

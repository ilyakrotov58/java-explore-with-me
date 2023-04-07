package ru.practicum.mapper;

import ru.practicum.dto.HitDto;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HitDtoMapper {
    public static Hit fromDto(HitDto hitDto) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        var convertedTimestamp = LocalDateTime.parse(hitDto.getTimestamp(), formatter);

        return new Hit(
                0,
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                convertedTimestamp);
    }

    public static HitDto toDto(Hit hit) {
        return new HitDto(
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                String.valueOf(hit.getTimestamp())
        );
    }
}

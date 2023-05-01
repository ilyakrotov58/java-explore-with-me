package ru.practicum.service.pub.interfaces;

import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;

import java.util.List;

public interface IEventsPublicService {

    List<EventShortDto> getEventList(
            @RequestParam @Nullable String text,
            @RequestParam @Nullable int[] categories,
            @RequestParam @Nullable Boolean paid,
            @RequestParam @Nullable String rangeStart,
            @RequestParam @Nullable String rangeEnd,
            @RequestParam(defaultValue = "false") boolean onlyAvailable,
            @RequestParam @Nullable String sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size);

    EventFullDto getEvent(
            @PathVariable long id);
}

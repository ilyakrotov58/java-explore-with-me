package ru.practicum.service.admin.interfaces;

import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.requests.UpdateEventAdminRequest;

import java.util.List;

public interface IEventAdminService {

    List<EventFullDto> getEventsList(
            @RequestParam long[] users,
            @RequestParam String[] states,
            @RequestParam long[] categories,
            @RequestParam String rangeStart,
            @RequestParam String rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size);

    EventFullDto updateEvent(
            @PathVariable long eventId,
            @RequestBody UpdateEventAdminRequest updateEventAdminRequest);
}

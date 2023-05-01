package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.requests.UpdateEventAdminRequest;
import ru.practicum.service.admin.interfaces.IEventAdminService;

import java.util.List;

@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/events")
@Slf4j
@RestController
public class EventsAdminController {

    private IEventAdminService eventAdminService;

    @Autowired
    public EventsAdminController(IEventAdminService eventAdminService) {
        this.eventAdminService = eventAdminService;
    }

    @GetMapping
    public List<EventFullDto> getEventsList(
            @RequestParam @Nullable long[] users,
            @RequestParam @Nullable String[] states,
            @RequestParam @Nullable long[] categories,
            @RequestParam @Nullable String rangeStart,
            @RequestParam @Nullable String rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        return eventAdminService.getEventsList(
                users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                from,
                size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(
            @PathVariable int eventId,
            @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return eventAdminService.updateEvent(
                eventId,
                updateEventAdminRequest);
    }
}

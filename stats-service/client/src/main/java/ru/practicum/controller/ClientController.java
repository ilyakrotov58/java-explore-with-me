package ru.practicum.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.HitDto;
import ru.practicum.service.ClientService;

import javax.validation.Valid;

@RequiredArgsConstructor
@Validated
@Controller
@Slf4j
public class ClientController {

    private ClientService service;

    @Autowired
    public ClientController(ClientService service) {
        this.service = service;
    }

    @PostMapping("/hit")
    @Operation(summary = "Add endpoint request")
    public ResponseEntity<Object> saveViewInfo(@Valid @RequestBody HitDto hitDto) {
        return service.saveViewInfo(hitDto);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get statistic about endpoint requests")
    public ResponseEntity<Object> getStatistic(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam @Nullable String[] uris,
            @RequestParam @Nullable Boolean unique) {

        if (uris == null) {
            return service.getStatistic(start, end, unique);
        } else {
            return service.getStatistic(start, end, uris, unique);
        }
    }
}

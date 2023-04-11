package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatInfoDto;
import ru.practicum.service.ServerService;

import java.util.List;

@RestController
@Slf4j
public class ServerController {

    private final ServerService statService;

    @Autowired
    public ServerController(ServerService statService) {
        this.statService = statService;
    }

    @PostMapping("/hit")
    public void saveViewInfo(@RequestBody HitDto hitDto) {
        statService.saveViewInfo(hitDto);
    }

    @GetMapping("/stats")
    public List<StatInfoDto> getStatistic(@RequestParam String start,
                                          @RequestParam String end,
                                          @RequestParam @Nullable String[] uris,
                                          @RequestParam @Nullable boolean unique) {
        return statService.getStatistic(start, end, uris, unique);
    }
}

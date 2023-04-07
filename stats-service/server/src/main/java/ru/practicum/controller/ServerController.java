package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.model.StatInfo;
import ru.practicum.service.ServerService;

import java.util.ArrayList;

@RestController
@Slf4j
public class ServerController {

    @Autowired
    private ServerService statService;

    @PostMapping("/hit")
    public void saveViewInfo(@RequestBody HitDto hitDto) {
        statService.saveViewInfo(hitDto);
    }

    @GetMapping("/stats")
    public ArrayList<StatInfo> getStatistic(@RequestParam String start,
                                            @RequestParam String end,
                                            @RequestParam @Nullable String[] uris,
                                            @RequestParam @Nullable boolean unique) {
        return statService.getStatistic(start, end, uris, unique);
    }
}

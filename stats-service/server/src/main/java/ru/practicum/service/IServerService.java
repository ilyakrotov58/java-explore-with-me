package ru.practicum.service;

import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatInfoDto;

import java.text.ParseException;
import java.util.List;

public interface IServerService {

    void saveViewInfo(HitDto hit) throws ParseException;

    List<StatInfoDto> getStatistic(String start, String end, String[] uris, boolean unique);
}

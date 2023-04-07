package ru.practicum.service;

import ru.practicum.dto.HitDto;
import ru.practicum.model.StatInfo;

import java.text.ParseException;
import java.util.ArrayList;

public interface IServerService {

    void saveViewInfo(HitDto hit) throws ParseException;

    ArrayList<StatInfo> getStatistic(String start, String end, String[] uris, boolean unique);
}

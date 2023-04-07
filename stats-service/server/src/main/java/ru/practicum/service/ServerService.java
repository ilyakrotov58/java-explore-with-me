package ru.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.HitDto;
import ru.practicum.mapper.HitDtoMapper;
import ru.practicum.model.StatInfo;
import ru.practicum.repository.IServerRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Slf4j
@Service
@Transactional
public class ServerService implements IServerService {

    @Autowired
    private IServerRepository serverRepository;

    @Transactional
    @Override
    public void saveViewInfo(HitDto hitDto) {

        var hit = HitDtoMapper.fromDto(hitDto);

        serverRepository.save(hit);
    }

    @Override
    public ArrayList<StatInfo> getStatistic(String start, String end, String[] uris, boolean unique) {

        ArrayList<StatInfo> result;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        var startDateTime = LocalDateTime.parse(start, formatter);
        var endDateTime = LocalDateTime.parse(end, formatter);

        if(uris != null) {
            if(unique) {
                result = serverRepository.getStatisticOfUriUniqIp(
                        startDateTime,
                        endDateTime,
                        uris);
            } else {
                result = serverRepository.getStatisticOfUriAllIp(
                        startDateTime,
                        endDateTime,
                        uris);
            }
        } else {
            if(unique) {
                result = serverRepository.getStatisticWithoutUriAndWithUniqueIp(
                        startDateTime,
                        endDateTime);
            } else {
                result = serverRepository.getStatisticWithoutUriAndUniqueIp(
                        startDateTime,
                        endDateTime);
            }
        }

        return result;
    }
}

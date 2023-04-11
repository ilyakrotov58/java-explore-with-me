package ru.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.HitDto;
import ru.practicum.exceptions.WrongDateTimeFormatException;
import ru.practicum.mapper.HitDtoMapper;
import ru.practicum.dto.StatInfoDto;
import ru.practicum.repository.IServerRepository;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ServerService implements IServerService {

    private final IServerRepository serverRepository;

    @Autowired
    public ServerService(IServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    @Transactional
    @Override
    public void saveViewInfo(HitDto hitDto) {

        var hit = HitDtoMapper.fromDto(hitDto);

        serverRepository.save(hit);
    }

    @Override
    public List<StatInfoDto> getStatistic(String start, String end, String[] uris, boolean unique) {

        List<StatInfoDto> result;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime startDateTime;
        LocalDateTime endDateTime;

        try {
            startDateTime = LocalDateTime.parse(start, formatter);
            endDateTime = LocalDateTime.parse(end, formatter);
        } catch (DateTimeException ex) {
            throw new WrongDateTimeFormatException("DateTime format of start/end date should be: \"yyyy-MM-dd HH:mm:ss\"");
        }

        if (uris != null) {
            if (unique) {
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
            if (unique) {
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

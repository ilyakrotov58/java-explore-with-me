package ru.practicum.service.pub.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.mappers.CompilationDtoMapper;
import ru.practicum.dto.mappers.EventDtoMapper;
import ru.practicum.model.Event;
import ru.practicum.repository.pub.ICompilationPublicRepository;
import ru.practicum.repository.pub.IEventPublicRepository;
import ru.practicum.service.pub.interfaces.ICompilationPublicService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CompilationPublicService implements ICompilationPublicService {

    public final ICompilationPublicRepository compilationRepository;
    public final IEventPublicRepository eventPublicRepository;

    @Autowired
    public CompilationPublicService(
            ICompilationPublicRepository compilationRepository,
            IEventPublicRepository eventPublicRepository) {
        this.compilationRepository = compilationRepository;
        this.eventPublicRepository = eventPublicRepository;
    }

    @Override
    public List<CompilationDto> getCompilationList(Boolean pinned, int from, int size) {

        var compilations = new ArrayList<>(compilationRepository.getCompilationList(from, size));

        List<CompilationDto> compilationDtoList;

        if (pinned != null) {
            compilationDtoList = compilations
                    .stream()
                    .filter(c -> c.isPinned() == pinned)
                    .map(CompilationDtoMapper::toCompilationDto)
                    .collect(Collectors.toList());
        } else {
            compilationDtoList = compilations.stream()
                    .map(CompilationDtoMapper::toCompilationDto)
                    .collect(Collectors.toList());
        }

        for (CompilationDto compilationDto : compilationDtoList) {
            var events = eventPublicRepository.getEventsByCompilationId(compilationDto.getId());
            var eventsShortDto = new ArrayList<CompilationDto.EventShortDto>();

            for (Event event : events) {
                eventsShortDto.add(EventDtoMapper.toCompilationDtoEventShortDto(event));
            }

            compilationDto.setEvents(eventsShortDto);
        }

        return compilationDtoList;
    }

    @Override
    public CompilationDto getCompilation(long compId) {
        return CompilationDtoMapper
                .toCompilationDto(compilationRepository.getReferenceById(compId));
    }
}

package ru.practicum.dto.mappers;

import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.model.Compilation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CompilationDtoMapper {

    public static CompilationDto toCompilationDto(Compilation compilation) {

        List<CompilationDto.EventShortDto> events = new ArrayList<>();

        if (compilation.getEventsList() != null && compilation.getEventsList().size() != 0) {
            events = compilation.getEventsList()
                    .stream()
                    .map(EventDtoMapper::toCompilationDtoEventShortDto)
                    .collect(Collectors.toList());
        }

        return new CompilationDto(
                compilation.getId(),
                compilation.isPinned(),
                compilation.getTitle(),
                events);
    }

    public static Compilation fromNewToCompilation(NewCompilationDto newCompilationDto) {
        return new Compilation(
                0,
                newCompilationDto.isPinned(),
                newCompilationDto.getTitle(),
                null);
    }
}

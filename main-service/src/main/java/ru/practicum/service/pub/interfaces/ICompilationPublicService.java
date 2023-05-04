package ru.practicum.service.pub.interfaces;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.CompilationDto;

import java.util.List;


public interface ICompilationPublicService {
    List<CompilationDto> getCompilationList(
            @RequestParam Boolean pinned,
            @RequestParam int from,
            @RequestParam int size);

    CompilationDto getCompilation(
            @PathVariable long compId);
}

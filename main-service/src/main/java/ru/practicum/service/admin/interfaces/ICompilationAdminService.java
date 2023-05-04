package ru.practicum.service.admin.interfaces;

import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.requests.UpdateCompilationRequest;

public interface ICompilationAdminService {

    CompilationDto createCompilation(
            @RequestBody NewCompilationDto newCompilationDto);

    void deleteCompilation(@PathVariable long compId);

    CompilationDto updateCompilation(
            @PathVariable long compId,
            @RequestBody UpdateCompilationRequest updateCompilationRequest);
}

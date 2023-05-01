package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.requests.UpdateCompilationRequest;
import ru.practicum.service.admin.interfaces.ICompilationAdminService;

import javax.validation.Valid;

@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/compilations")
@Slf4j
@RestController
public class CompilationsAdminController {

    private ICompilationAdminService compilationAdminService;

    @Autowired
    public CompilationsAdminController(ICompilationAdminService compilationAdminService) {
        this.compilationAdminService = compilationAdminService;
    }

    @PostMapping
    public ResponseEntity<CompilationDto> createCompilation(
            @Valid
            @RequestBody NewCompilationDto newCompilationDto) {
        return new ResponseEntity<>(
                compilationAdminService.createCompilation(newCompilationDto),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<HttpStatus> deleteCompilation(@PathVariable int compId) {
        compilationAdminService.deleteCompilation(compId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(
            @PathVariable int compId,
            @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        return compilationAdminService.updateCompilation(compId, updateCompilationRequest);
    }
}

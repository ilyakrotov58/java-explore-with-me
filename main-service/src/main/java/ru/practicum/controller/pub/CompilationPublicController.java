package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.service.pub.interfaces.ICompilationPublicService;

import java.util.List;

@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/compilations")
@Slf4j
@RestController
public class CompilationPublicController {

    private ICompilationPublicService compilationPublicService;

    @Autowired
    public CompilationPublicController(ICompilationPublicService compilationPublicService) {
        this.compilationPublicService = compilationPublicService;
    }

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getCompilationList(
            @RequestParam @Nullable Boolean pinned,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                compilationPublicService.getCompilationList(pinned, from, size),
                HttpStatus.OK);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilation(
            @PathVariable int compId) {
        return new ResponseEntity<>(
                compilationPublicService.getCompilation(compId),
                HttpStatus.OK);
    }
}

package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.service.pub.interfaces.ICategoriesPublicService;

import java.util.List;

@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/categories")
@Slf4j
@RestController
public class CategoriesPublicController {

    private ICategoriesPublicService categoriesPublicService;

    @Autowired
    public CategoriesPublicController(ICategoriesPublicService categoriesPublicService) {
        this.categoriesPublicService = categoriesPublicService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategoriesList(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                categoriesPublicService.getCategoriesList(from, size),
                HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategory(
            @PathVariable int catId) {
        return new ResponseEntity<>(
                categoriesPublicService.getCategory(catId),
                HttpStatus.OK);
    }
}

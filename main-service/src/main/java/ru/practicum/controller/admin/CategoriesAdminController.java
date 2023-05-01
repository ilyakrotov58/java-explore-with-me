package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.service.admin.interfaces.ICategoriesAdminService;

import javax.validation.Valid;

@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/categories")
@Slf4j
@RestController
public class CategoriesAdminController {

    private ICategoriesAdminService categoriesAdminService;

    @Autowired
    public CategoriesAdminController(ICategoriesAdminService categoriesAdminService) {
        this.categoriesAdminService = categoriesAdminService;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(
            @Valid
            @RequestBody NewCategoryDto newCategoryDto) {
        return new ResponseEntity<>(categoriesAdminService.addCategory(newCategoryDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable int catId) {
        categoriesAdminService.deleteCategory(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(
            @PathVariable int catId,
            @Valid
            @RequestBody NewCategoryDto newCategoryDto) {
        return categoriesAdminService.updateCategory(
                catId,
                newCategoryDto);
    }
}

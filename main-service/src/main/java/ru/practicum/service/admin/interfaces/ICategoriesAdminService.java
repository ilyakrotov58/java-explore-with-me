package ru.practicum.service.admin.interfaces;

import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;

public interface ICategoriesAdminService {

    CategoryDto addCategory(
            @RequestBody NewCategoryDto newCategoryDto);

    void deleteCategory(@PathVariable long catId);

    CategoryDto updateCategory(
            @PathVariable long catId,
            @RequestBody NewCategoryDto newCategoryDto);
}

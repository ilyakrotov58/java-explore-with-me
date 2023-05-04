package ru.practicum.dto.mappers;

import ru.practicum.dto.CategoryDto;
import ru.practicum.model.Category;

public class CategoryDtoMapper {

    public static CategoryDto categoryToDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }
}

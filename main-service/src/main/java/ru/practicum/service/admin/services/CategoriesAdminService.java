package ru.practicum.service.admin.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.dto.mappers.CategoryDtoMapper;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ConflictDataException;
import ru.practicum.model.Category;
import ru.practicum.repository.admin.ICategoryAdminRepository;
import ru.practicum.repository.admin.IEventAdminRepository;
import ru.practicum.service.admin.interfaces.ICategoriesAdminService;

@Slf4j
@Service
public class CategoriesAdminService implements ICategoriesAdminService {

    private final ICategoryAdminRepository categoryAdminRepository;
    private final IEventAdminRepository eventAdminRepository;

    @Autowired
    public CategoriesAdminService(
            ICategoryAdminRepository categoryAdminRepository,
            IEventAdminRepository eventAdminRepository) {
        this.categoryAdminRepository = categoryAdminRepository;
        this.eventAdminRepository = eventAdminRepository;
    }

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {

        var categories = categoryAdminRepository.findAll();

        if (categories.stream().anyMatch(c -> c.getName().equals(newCategoryDto.getName()))) {
            throw new ConflictDataException("Category with this name have been already saved");
        }

        return CategoryDtoMapper.categoryToDto(
                categoryAdminRepository.save(
                        new Category(
                                0, newCategoryDto.getName())));
    }

    @Override
    public void deleteCategory(long catId) {

        if (eventAdminRepository.findAll().stream().anyMatch(e -> e.getCategory().getId() == catId)) {
            throw new ConflictDataException("Can't delete category with events");
        }

        categoryAdminRepository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(long catId, NewCategoryDto newCategoryDto) {

        var category = categoryAdminRepository.findById(catId);

        if (category.isEmpty()) {
            throw new NotFoundException(String.format("Category with id = %s is not found", catId));
        }

        var categories = categoryAdminRepository.findAll();

        if (categories.stream().anyMatch(c -> c.getName().equals(newCategoryDto.getName()))) {
            throw new ConflictDataException("Trying to change to the same name");
        }

        if (newCategoryDto.getName() != null) {
            category.get().setName(newCategoryDto.getName());
        }

        return CategoryDtoMapper.categoryToDto(category.get());
    }
}

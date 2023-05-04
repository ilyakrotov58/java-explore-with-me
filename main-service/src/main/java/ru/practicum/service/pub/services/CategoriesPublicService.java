package ru.practicum.service.pub.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.mappers.CategoryDtoMapper;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.repository.pub.ICategoriesPublicRepository;
import ru.practicum.service.pub.interfaces.ICategoriesPublicService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoriesPublicService implements ICategoriesPublicService {

    final ICategoriesPublicRepository categoriesPublicRepository;

    @Autowired
    public CategoriesPublicService(ICategoriesPublicRepository categoriesPublicRepository) {
        this.categoriesPublicRepository = categoriesPublicRepository;
    }

    @Override
    public List<CategoryDto> getCategoriesList(int from, int size) {

        return categoriesPublicRepository.findAllWithParams(from, size)
                .stream()
                .map(CategoryDtoMapper::categoryToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(long catId) {

        var category = categoriesPublicRepository.findById(catId);

        if (category.isPresent()) {
            return CategoryDtoMapper.categoryToDto(category.get());
        } else {
            throw new NotFoundException(
                    String.format("Category with id = %s can't be found", catId));
        }
    }
}

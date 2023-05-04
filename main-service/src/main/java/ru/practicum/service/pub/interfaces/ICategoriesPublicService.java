package ru.practicum.service.pub.interfaces;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.CategoryDto;

import java.util.List;

public interface ICategoriesPublicService {

    List<CategoryDto> getCategoriesList(
            @RequestParam int from,
            @RequestParam int size);

    CategoryDto getCategory(
            @PathVariable long catId);
}

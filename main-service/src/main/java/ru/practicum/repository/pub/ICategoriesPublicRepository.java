package ru.practicum.repository.pub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Category;

import java.util.List;

public interface ICategoriesPublicRepository extends JpaRepository<Category, Long> {

    @Query(value =
            "SELECT * FROM categories " +
                    "ORDER BY id " +
                    "LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Category> findAllWithParams(int from, int size);
}


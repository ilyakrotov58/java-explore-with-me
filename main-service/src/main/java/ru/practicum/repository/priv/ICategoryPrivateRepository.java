package ru.practicum.repository.priv;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Category;

public interface ICategoryPrivateRepository extends JpaRepository<Category, Long> {
}

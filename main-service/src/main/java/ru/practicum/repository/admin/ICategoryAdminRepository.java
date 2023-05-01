package ru.practicum.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Category;

public interface ICategoryAdminRepository extends JpaRepository<Category, Long> {
}

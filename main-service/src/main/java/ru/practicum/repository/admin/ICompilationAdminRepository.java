package ru.practicum.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Compilation;

public interface ICompilationAdminRepository extends JpaRepository<Compilation, Long> {
}

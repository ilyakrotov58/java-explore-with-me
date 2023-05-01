package ru.practicum.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Location;

public interface ILocationAdminRepository extends JpaRepository<Location, Long> {
}

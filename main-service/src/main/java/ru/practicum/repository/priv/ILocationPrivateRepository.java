package ru.practicum.repository.priv;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Location;

public interface ILocationPrivateRepository extends JpaRepository<Location, Long> {
}

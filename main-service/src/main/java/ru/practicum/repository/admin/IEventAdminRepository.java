package ru.practicum.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Event;

public interface IEventAdminRepository extends JpaRepository<Event, Long> {
}

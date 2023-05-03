package ru.practicum.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Event;

import java.util.List;

public interface IEventAdminRepository extends JpaRepository<Event, Long> {

    @Query(value =
            "SELECT * FROM events " +
            "WHERE id IN (?1)", nativeQuery = true)
    List<Event> getEventsByIds(List<Long> ids);

}

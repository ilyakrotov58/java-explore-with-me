package ru.practicum.repository.pub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Event;

import java.util.List;

public interface IEventPublicRepository extends JpaRepository<Event, Long> {

    @Query(value =
            "SELECT * FROM events " +
                    "INNER JOIN compilations_events ce on events.id = ce.event_id " +
                    "WHERE compilation_id = ?1",
            nativeQuery = true)
    List<Event> getEventsByCompilationId(long compilationId);

    @Query(value =
            "SELECT * FROM events AS e " +
                    "LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Event> getEvents(int from, int size);
}

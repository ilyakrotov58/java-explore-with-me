package ru.practicum.repository.priv;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Event;

import java.util.List;

public interface IEventPrivateRepository extends JpaRepository<Event, Long> {

    @Query(value =
            "SELECT * FROM events " +
                    "WHERE initiator_id = ?1 " +
                    "ORDER BY id " +
                    "LIMIT ?3 OFFSET ?2 ", nativeQuery = true)
    List<Event> getEventsAddedByCurrentUser(long userId, int from, int size);
}

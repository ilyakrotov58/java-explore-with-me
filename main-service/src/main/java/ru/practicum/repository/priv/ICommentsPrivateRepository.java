package ru.practicum.repository.priv;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Comment;

import java.util.List;

public interface ICommentsPrivateRepository extends JpaRepository<Comment, Long> {

    @Query(value =
            "SELECT * FROM comments " +
                    "WHERE event_id = ?1", nativeQuery = true)
    List<Comment> getAllComments(long eventId);

    @Query(value =
            "SELECT * FROM comments " +
                    "WHERE event_id = ?1 AND user_id = ?2", nativeQuery = true)
    List<Comment> getAllUserComments(long eventId, long userId);
}

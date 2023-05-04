package ru.practicum.repository.pub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Comment;

import java.util.List;

public interface ICommentsPublicRepository extends JpaRepository<Comment, Long> {

    @Query(value =
            "SELECT * FROM comments " +
                    "WHERE event_id = ?1", nativeQuery = true)
    List<Comment> getAllComments(long eventId);

    @Query(value =
            "SELECT * FROM comments " +
                    "WHERE id = ?1", nativeQuery = true)
    Comment getCommentById(long commentId);
}

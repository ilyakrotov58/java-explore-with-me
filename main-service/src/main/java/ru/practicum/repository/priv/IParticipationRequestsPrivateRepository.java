package ru.practicum.repository.priv;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.ParticipationRequest;

import java.util.List;

public interface IParticipationRequestsPrivateRepository extends JpaRepository<ParticipationRequest, Long> {

    @Query(value =
            "SELECT * FROM participation_requests " +
                    "WHERE requester_id = ?1 AND event_id = ?2", nativeQuery = true)
    List<ParticipationRequest> getParticipationRequests(long userId, long eventId);

    @Query(value =
            "SELECT * FROM participation_requests " +
                    "WHERE requester_id = ?1", nativeQuery = true)
    List<ParticipationRequest> getUserParticipationRequests(long userId);

    @Query(value =
            "SELECT * FROM participation_requests " +
                    "WHERE event_id IN(?1)", nativeQuery = true)
    List<ParticipationRequest> getUserParticipationRequestsByEventId(List<Long> eventIds);

    @Query(value =
            "SELECT * FROM participation_requests " +
            "WHERE id IN(?1)", nativeQuery = true)
    List<ParticipationRequest> getUserParticipationRequestsByIds(List<Long> requestsIds);

}

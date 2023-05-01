package ru.practicum.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.domain.RequestState;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {

    private long id;

    private LocalDateTime created;

    private long event;

    private long requester;

    private RequestState status;
}

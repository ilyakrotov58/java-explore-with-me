package ru.practicum.dto.requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.domain.RequestState;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {

    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;

    @Data
    @AllArgsConstructor
    public static class ParticipationRequestDto {

        private long id;

        private long event;

        private String created;

        private long requester;

        private RequestState status;
    }
}

package ru.practicum.controller.pub;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.HitDto;
import ru.practicum.service.pub.interfaces.IEventsPublicService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/events")
@Slf4j
@RestController
public class EventsPublicController {

    private final Gson gson = new Gson();
    private IEventsPublicService eventsPublicService;

    @Autowired
    public EventsPublicController(IEventsPublicService eventsPublicService) {
        this.eventsPublicService = eventsPublicService;

    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEventList(
            @RequestParam @Nullable String text,
            @RequestParam @Nullable int[] categories,
            @RequestParam @Nullable Boolean paid,
            @RequestParam @Nullable String rangeStart,
            @RequestParam @Nullable String rangeEnd,
            @RequestParam(defaultValue = "false") boolean onlyAvailable,
            @RequestParam @Nullable String sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) throws IOException {

        sendRequestToStatService(request);
        return new ResponseEntity<>(
                eventsPublicService.getEventList(
                        text,
                        categories,
                        paid,
                        rangeStart,
                        rangeEnd,
                        onlyAvailable,
                        sort,
                        from,
                        size),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getEvent(
            @PathVariable long id,
            HttpServletRequest request) throws UnsupportedEncodingException {
        sendRequestToStatService(request);
        return new ResponseEntity<>(
                eventsPublicService.getEvent(id),
                HttpStatus.OK);
    }

    private void sendRequestToStatService(HttpServletRequest request) throws UnsupportedEncodingException {

        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateFormatted = LocalDateTime.now().format(f);

        var ip = request.getRemoteAddr();

        final String json = gson.toJson(new HitDto(
                "ewm-main-service",
                request.getRequestURI(),
                ip,
                dateFormatted
        ));
        final StringEntity entity = new StringEntity(json);

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest httppost = RequestBuilder.post()
                    .setHeader("Accept", "application/json")
                    .setHeader("Content-type", "application/json")
                    .setUri(new URI("http://server:9090/hit"))
                    .setEntity(entity)
                    .build();

            try (CloseableHttpResponse response = httpclient.execute(httppost)) {
                System.out.println(EntityUtils.toString(response.getEntity()));
            }
        } catch (URISyntaxException | IOException ex) {
            log.error(ex.getMessage());
        }
    }
}

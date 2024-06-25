package ru.practicum.ewm.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.StatsDto;

import javax.websocket.Endpoint;
import java.time.LocalDateTime;

public class StatsClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String url = "http://localhost:9090";

    HttpStatus post(String app, String uri, String ip, LocalDateTime timestamp) {
//        EndpointHitDto body = new EndpointHitDto(app, uri, ip, timestamp.format(HitMapper));
        String uriString = UriComponentsBuilder.fromUriString(url + "/hit")
//                .queryParam("state", "ALL")
//                .queryParam("from", 10)
//                .queryParam("size", 20)
                .toUriString();
//        HttpEntity<String> entity = new HttpEntity<>(body.toString());
        return HttpStatus.ACCEPTED;
    }


}

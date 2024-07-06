package ru.practicum.ewm.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.ewm.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class StatsClient {
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${ewm.stats.server.url}")
    private String url;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");



    public void post(String app, String uri, String ip, LocalDateTime timestamp) {
        String uriString = UriComponentsBuilder.fromUriString(url + "/hit")
                .toUriString();

        EndpointHitDto body = new EndpointHitDto(app,
                uri,
                ip,
                timestamp.format(formatter));

        makeAndSendRequest(HttpMethod.POST, uriString, body);
    }

    public ResponseEntity<Object> get(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        String uriString;
        String startEncoded = start.format(formatter);
        String endEncoded = end.format(formatter);

        if (uris == null) {
            uriString = UriComponentsBuilder.fromUriString(url + "/stats")
                    .queryParam("start", startEncoded)
                    .queryParam("end", endEncoded)
                    .queryParam("unique", unique)
                    .toUriString();
        } else {
            uriString = UriComponentsBuilder.fromUriString(url + "/stats")
                    .queryParam("start", startEncoded)
                    .queryParam("end", endEncoded)
                    .queryParam("uris", uris)
                    .queryParam("unique", unique)
                    .toUriString();
        }
        return makeAndSendRequest(HttpMethod.GET, uriString, null);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, @Nullable T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<T> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Object> statsServerResponse;
        try {
            statsServerResponse = restTemplate.exchange(path, method, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareStatsClientResponse(statsServerResponse);
    }

    private static ResponseEntity<Object> prepareStatsClientResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }
        return responseBuilder.build();
    }
}

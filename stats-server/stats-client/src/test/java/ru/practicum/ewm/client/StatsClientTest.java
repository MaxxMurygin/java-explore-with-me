package ru.practicum.ewm.client;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.StatsDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class StatsClientTest {

    StatsClient client = new StatsClient();

    @Test
    void post() {
        ResponseEntity<Object> answer = client.post("client", "/test/2", "100.1.1.1", LocalDateTime.now());
        System.out.println();
    }
    @Test
    void get() {
        String[] uris = {"/test/2", "/events/1"};
        ResponseEntity<Object> stat = client.get(LocalDateTime.now().minusYears(2), LocalDateTime.now().plusYears(2), uris, true);
        System.out.println();
    }
}
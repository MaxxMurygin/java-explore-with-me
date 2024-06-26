package ru.practicum.ewm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.service.HitService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HitsController.class)
class HitsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private HitService hitService;
    @Autowired
    private ObjectMapper objectMapper;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String start;
    String end;

    @BeforeEach
    void setUp() {
        start = LocalDateTime.now().minusDays(1).format(formatter);
        end = LocalDateTime.now().plusDays(1).format(formatter);
    }

    @Test
    @SneakyThrows
    void addHit() {
        EndpointHitDto hitDto = new EndpointHitDto("testApp",
                "/enpoint/1",
                "10.1.1.1",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        doNothing().when(hitService).create(hitDto);

        mockMvc.perform(post("/hit")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hitDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @SneakyThrows
    void addHitWithWrongIp() {
        EndpointHitDto hitDto = new EndpointHitDto("testApp",
                "/enpoint/1",
                "10.666.1.1",
                LocalDateTime.now().format(formatter));
        doNothing().when(hitService).create(hitDto);

        mockMvc.perform(post("/hit")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hitDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void getStats() {

        String uriString = UriComponentsBuilder.fromUriString("http://localhost:9090/stats")
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("unique", true)
                .toUriString();
        when(hitService.getStats(start, end, true)).thenReturn(new ArrayList<>());

        mockMvc.perform(get(uriString)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void getStatsWithEndBeforeStart() {

        String uriString = UriComponentsBuilder.fromUriString("http://localhost:9090/stats")
                .queryParam("start", end)
                .queryParam("end", start)
                .queryParam("unique", true)
                .toUriString();
        when(hitService.getStats(start, end, true)).thenReturn(new ArrayList<>());

        mockMvc.perform(get(uriString)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void getStatsWithStartInFuture() {

        String uriString = UriComponentsBuilder.fromUriString("http://localhost:9090/stats")
                .queryParam("start", LocalDateTime.now().plusDays(1))
                .queryParam("end", LocalDateTime.now().plusDays(2))
                .queryParam("unique", true)
                .toUriString();
        when(hitService.getStats(start, end, true)).thenReturn(new ArrayList<>());

        mockMvc.perform(get(uriString)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
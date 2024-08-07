package ru.practicum.ewm.repository;

import ru.practicum.ewm.dto.StatsDto;
import ru.practicum.ewm.model.EndpointHit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class HitRepositoryTest {
    @Autowired
    private HitRepository repository;
    private EndpointHit hit;
    private final LocalDateTime time = LocalDateTime.of(2024, 1, 1, 0, 0, 1);


    @BeforeEach
    public void setUp() {
        hit = new EndpointHit();

        hit.setApp("testApp");
        hit.setUri("/hit/1");
        hit.setIp("10.1.1.1");
        hit.setCreated(LocalDateTime.now());
    }

    @Test
    void add() {
        EndpointHit stored = repository.save(hit);

        assertNotNull(stored);
    }

    @Test
    void findByStartAndEnd() {
        List<StatsDto> stats = repository.findByStartAndEnd(time, time.plusDays(1));
        assertNotNull(stats);
        assertEquals(stats.size(), 3);
        assertEquals(stats.get(0).getHits(), 3);
        assertEquals(stats.get(1).getHits(), 2);
        assertEquals(stats.get(2).getHits(), 2);
    }

    @Test
    void findByStartAndEndAndUnique() {
        List<StatsDto> stats = repository.findByStartAndEndAndUnique(time, time.plusDays(1));
        assertNotNull(stats);
        assertEquals(stats.size(), 3);
        assertEquals(stats.get(0).getHits(), 2);
        assertEquals(stats.get(1).getHits(), 2);
        assertEquals(stats.get(2).getHits(), 1);
    }

    @Test
    void findByStartAndEndAndUriIn() {
        String[] uris = {"/events/1", "/events/2"};
        List<StatsDto> stats = repository.findByStartAndEndAndUriIn(time, time.plusDays(1), uris);
        assertNotNull(stats);
        assertEquals(stats.size(), 2);
        assertEquals(stats.get(0).getHits(), 3);
        assertEquals(stats.get(1).getHits(), 2);
    }

    @Test
    void findByStartAndEndAndUniqueAndUriIn() {
        String[] uris = {"/events/1", "/events/2"};
        List<StatsDto> stats = repository.findByStartAndEndAndUniqueAndUriIn(time, time.plusDays(1), uris);
        assertNotNull(stats);
        assertEquals(stats.size(), 2);
        assertEquals(stats.get(0).getHits(), 2);
        assertEquals(stats.get(1).getHits(), 2);
    }
}
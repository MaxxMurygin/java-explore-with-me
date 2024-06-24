package com.github.maxxmurugin.ewm.repository;

import com.github.maxxmurugin.ewm.model.EndpointHit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class HitRepositoryTest {
    @Autowired
    private HitRepository repository;
    private EndpointHit hit;

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

}
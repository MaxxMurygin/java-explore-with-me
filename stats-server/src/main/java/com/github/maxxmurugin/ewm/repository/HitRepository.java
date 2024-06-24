package com.github.maxxmurugin.ewm.repository;

import com.github.maxxmurugin.ewm.model.EndpointHit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HitRepository extends JpaRepository<EndpointHit, Long> {
    EndpointHit findByIp(String ip);
}

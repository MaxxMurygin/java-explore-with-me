package repository;

import model.EndpointHit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HitRepository extends JpaRepository<EndpointHit, Long> {
    EndpointHit findByIp(String ip);
}

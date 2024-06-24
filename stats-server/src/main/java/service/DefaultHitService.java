package service;

import dto.EndpointHitDto;
import lombok.RequiredArgsConstructor;
import model.EndpointHit;
import model.HitMapper;
import org.springframework.stereotype.Service;
import repository.HitRepository;

@Service
@RequiredArgsConstructor
public class DefaultHitService implements HitService {
    private final HitRepository hitRepository;

    @Override
    public void create(EndpointHitDto hitDto) {
        hitRepository.save(HitMapper.fromDto(hitDto));
    }
}

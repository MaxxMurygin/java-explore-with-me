package com.github.maxxmurugin.ewm.service;

import com.github.maxxmurugin.ewm.dto.EndpointHitDto;
import com.github.maxxmurugin.ewm.dto.StatsDto;
import lombok.RequiredArgsConstructor;
import com.github.maxxmurugin.ewm.model.HitMapper;
import org.springframework.stereotype.Service;
import com.github.maxxmurugin.ewm.repository.HitRepository;

@Service
@RequiredArgsConstructor
public class DefaultHitService implements HitService {
    private final HitRepository hitRepository;

    @Override
    public void create(EndpointHitDto hitDto) {
        hitRepository.save(HitMapper.fromDto(hitDto));
    }

    @Override
    public StatsDto getStats(String start, String end, String[] uris, Boolean unique) {
        return new StatsDto();
    }
}

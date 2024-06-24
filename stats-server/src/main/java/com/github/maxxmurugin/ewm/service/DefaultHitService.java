package com.github.maxxmurugin.ewm.service;

import com.github.maxxmurugin.ewm.dto.EndpointHitDto;
import com.github.maxxmurugin.ewm.dto.StatsDto;
import lombok.RequiredArgsConstructor;
import com.github.maxxmurugin.ewm.model.HitMapper;
import org.springframework.stereotype.Service;
import com.github.maxxmurugin.ewm.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultHitService implements HitService {
    private final HitRepository hitRepository;

    @Override
    public void create(EndpointHitDto hitDto) {
        hitRepository.save(HitMapper.fromDto(hitDto));
    }

    @Override
    public List<StatsDto> getStats(String start, String end, String[] uris, boolean unique) {
        if (!unique) {
            return hitRepository.findByStartAndEndAndUriIn(LocalDateTime.parse(start, HitMapper.formatter),
                                                            LocalDateTime.parse(start, HitMapper.formatter),
                                                            uris);
        }
        return hitRepository.findByStartAndEndAndUniqueAndUriIn(LocalDateTime.parse(start, HitMapper.formatter),
                                                                LocalDateTime.parse(start, HitMapper.formatter),
                                                                uris);
    }

    @Override
    public List<StatsDto> getStats(String start, String end, boolean unique) {
        if (!unique) {
            return hitRepository.findByStartAndEnd(LocalDateTime.parse(start, HitMapper.formatter),
                    LocalDateTime.parse(start, HitMapper.formatter));
        }
        return hitRepository.findByStartAndEndAndUnique(LocalDateTime.parse(start, HitMapper.formatter),
                LocalDateTime.parse(start, HitMapper.formatter));
    }
}

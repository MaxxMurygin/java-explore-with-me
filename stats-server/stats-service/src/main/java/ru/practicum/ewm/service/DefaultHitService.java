package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.StatsDto;
import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.model.HitMapper;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        LocalDateTime startTime = LocalDateTime.parse(start, HitMapper.formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, HitMapper.formatter);

        if (!unique) {
            return hitRepository.findByStartAndEndAndUriIn(startTime, endTime, uris);
        }
        return hitRepository.findByStartAndEndAndUniqueAndUriIn(startTime, endTime, uris);
    }

    @Override
    public List<StatsDto> getStats(String start, String end, boolean unique) {
        LocalDateTime startTime = LocalDateTime.parse(start, HitMapper.formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, HitMapper.formatter);

        if (!unique) {
            return hitRepository.findByStartAndEnd(startTime,endTime);
        }
        return hitRepository.findByStartAndEndAndUnique(startTime,endTime);
    }

    @Override
    public List<EndpointHitDto> hitDtos() {
        return hitRepository.findAll().stream().map(HitMapper::toDto).collect(Collectors.toList());
    }
}

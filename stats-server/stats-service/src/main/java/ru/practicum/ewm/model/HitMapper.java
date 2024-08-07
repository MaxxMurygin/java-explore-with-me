package ru.practicum.ewm.model;

import ru.practicum.ewm.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HitMapper {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EndpointHit fromDto(EndpointHitDto hitDto) {
        EndpointHit hit = new EndpointHit();

        hit.setApp(hitDto.getApp());
        hit.setUri(hitDto.getUri());
        hit.setIp(hitDto.getIp());
        hit.setCreated(LocalDateTime.parse(hitDto.getTimestamp(), formatter));
        return hit;

    }

    public static EndpointHitDto toDto(EndpointHit hit) {
        EndpointHitDto hitDto = new EndpointHitDto();

        hitDto.setApp(hit.getApp());
        hitDto.setUri(hit.getUri());
        hitDto.setIp(hit.getIp());
        hitDto.setTimestamp(hit.getCreated().format(formatter));
        return hitDto;
    }
}

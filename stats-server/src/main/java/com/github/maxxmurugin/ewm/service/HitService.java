package com.github.maxxmurugin.ewm.service;

import com.github.maxxmurugin.ewm.dto.EndpointHitDto;
import com.github.maxxmurugin.ewm.dto.StatsDto;

public interface HitService {
    void create(EndpointHitDto hitDto);

    StatsDto getStats(String start,
                      String end,
                      String[] uris,
                      Boolean unique);
}

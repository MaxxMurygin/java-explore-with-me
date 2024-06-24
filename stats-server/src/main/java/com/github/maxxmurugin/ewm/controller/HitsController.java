package com.github.maxxmurugin.ewm.controller;

import com.github.maxxmurugin.ewm.dto.EndpointHitDto;
import com.github.maxxmurugin.ewm.dto.StatsDto;
import com.github.maxxmurugin.ewm.service.HitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
@Validated
public class HitsController {
    private final HitService hitService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addHit(@Valid @RequestBody EndpointHitDto hitDto) {
        hitService.create(hitDto);
    }

    @GetMapping("/stats")
    public List<StatsDto> getStats(@RequestParam(name = "start") String startEncoded,
                                  @RequestParam(name = "end") String endEncoded,
                                  @RequestParam(name = "uris", required = false) String[] uris,
                                  @RequestParam(name = "unique", defaultValue = "false") boolean unique) {

        String start = URLDecoder.decode(startEncoded, StandardCharsets.UTF_8);
        String end = URLDecoder.decode(endEncoded, StandardCharsets.UTF_8);
        if (uris == null) {
            hitService.getStats(start, end, unique);
        }
        return hitService.getStats(start, end, uris, unique);

    }
}

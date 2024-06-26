package ru.practicum.ewm.controller;

import org.springframework.web.util.UriUtils;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.StatsDto;
import ru.practicum.ewm.model.HitMapper;
import ru.practicum.ewm.service.HitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.validators.RequestParamValidator;
import ru.practicum.ewm.validators.Validator;

import javax.validation.Valid;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
@Validated
public class HitsController {
    private final HitService hitService;
    private RequestParamValidator validator = new RequestParamValidator();

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

        validator.validate(Map.of("start", start, "end", end));

        if (uris == null) {
            return hitService.getStats(start, end, unique);
        }
        return hitService.getStats(start, end, uris, unique);
    }

    @GetMapping("/hi")
    public List<StatsDto> hi() {
        List<StatsDto> result = hitService.getStats(LocalDateTime.now().minusYears(1).format(HitMapper.formatter),
                LocalDateTime.now().plusYears(1).format(HitMapper.formatter),
                false);
        log.info(result.toString());
        return result;
    }
}

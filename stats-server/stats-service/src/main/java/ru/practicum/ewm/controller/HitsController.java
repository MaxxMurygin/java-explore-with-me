package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.StatsDto;
import ru.practicum.ewm.exceptions.ValidationException;
import ru.practicum.ewm.service.HitService;
import ru.practicum.ewm.validators.RequestParamValidator;

import javax.validation.Valid;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
@Validated
public class HitsController {
    private final HitService hitService;
    private final RequestParamValidator requestParamValidator = new RequestParamValidator();
    private final InetAddressValidator addressValidator = InetAddressValidator.getInstance();

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addHit(@Valid @RequestBody EndpointHitDto hitDto) {
        if (!addressValidator.isValid(hitDto.getIp())) {
            throw new ValidationException("Неправильный формат IP адреса");
        }
        hitService.create(hitDto);
    }

    @GetMapping("/stats")
    public List<StatsDto> getStats(@RequestParam(name = "start") String startEncoded,
                                   @RequestParam(name = "end") String endEncoded,
                                   @RequestParam(name = "uris", required = false) String[] uris,
                                   @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
        String start = URLDecoder.decode(startEncoded, StandardCharsets.UTF_8);
        String end = URLDecoder.decode(endEncoded, StandardCharsets.UTF_8);

        requestParamValidator.validate(Map.of("start", start, "end", end));

        if (uris == null) {
            return hitService.getStats(start, end, unique);
        }
        return hitService.getStats(start, end, uris, unique);
    }
}

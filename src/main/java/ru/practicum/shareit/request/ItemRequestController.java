package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ExtraItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.contexts.CreateItemRequestContext;
import ru.practicum.shareit.request.contexts.RetrieveItemRequestContext;
import ru.practicum.shareit.request.contexts.RetrieveItemRequestsForUserContext;
import ru.practicum.shareit.request.contexts.RetrieveItemRequestsContext;
import ru.practicum.shareit.request.service.ControllerItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ControllerItemRequestService service;

    @PostMapping
    public ItemRequestDto create(@Valid @RequestBody CreateItemRequestDto createItemRequestDto,
                                 @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("создание запроса о предоставлении вещи");

        CreateItemRequestContext context = CreateItemRequestContext.builder()
                .sharerUserId(userId)
                .createItemRequestDto(createItemRequestDto)
                .build();

        return service.create(context);
    }

    @GetMapping
    public List<ExtraItemRequestDto> retrieve(@RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("получение списка запросов о предоставлении вещей, созданных отдельным пользователем");

        RetrieveItemRequestsForUserContext context = RetrieveItemRequestsForUserContext.builder()
                .sharerUserId(userId)
                .build();

        return service.retrieve(context);
    }

    @GetMapping("/all")
    public List<ExtraItemRequestDto> retrieve(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                              @RequestParam(defaultValue = "15") @Min(0) Integer size,
                                              @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("получение общего списка запросов о преодставлении вещей (постранично)");

        RetrieveItemRequestsContext context = RetrieveItemRequestsContext.builder()
                .sharerUserId(userId)
                .from(from)
                .size(size)
                .build();

        return service.retrieve(context);
    }

    @GetMapping("/{requestId}")
    public ExtraItemRequestDto retrieve(@PathVariable Long requestId,
                                        @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("получение сведений о конкретном запросе о предоставлении вещи");

        RetrieveItemRequestContext context = RetrieveItemRequestContext.builder()
                .targetItemRequestId(requestId)
                .sharerUserId(userId)
                .build();

        return service.retrieve(context);
    }
}

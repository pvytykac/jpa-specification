package net.pvytykac.jpaspec.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pvytykac.jpaspec.db.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/v1/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemRepository repository;
    private final ItemMapper mapper;

    @GetMapping
    public Page<ItemGetDto> getItems(@Valid @NotNull ItemsFilter filter, Pageable pageable) {
        log.info("Getting items for filter: {} and pageable: {}", filter, pageable);

        return repository.findAll(filter, pageable)
                .map(mapper::dboToResponseDto);
    }

    @PostMapping
    public ItemGetDto filter(@RequestBody @Valid @NotNull ItemPostDto payload) {
        log.info("Saving item: {}", payload);

        var dbo = repository.save(mapper.postDtoToDbo(payload));

        return mapper.dboToResponseDto(dbo);
    }

    @GetMapping("/{itemId}")
    public ItemGetDto getItem(@PathVariable UUID itemId) {
        log.info("Getting item with id '{}'", itemId);

        return repository.findById(itemId)
                .map(mapper::dboToResponseDto)
                .orElseThrow(() -> getNotFoundError(itemId));
    }

    @DeleteMapping("/{itemId}")
    public ItemGetDto delete(@PathVariable UUID itemId) {
        log.info("Deleting item with id '{}'", itemId);

        var deleted = repository.findById(itemId);

        deleted.ifPresent(repository::delete);

        return deleted.map(mapper::dboToResponseDto)
                .orElseThrow(() -> getNotFoundError(itemId));
    }

    private ResponseStatusException getNotFoundError(UUID itemId) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Item with id " + itemId + " does not exist");
    }

}

package com.nixs.archetype.controller;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.nixs.archetype.dto.Item;
import com.nixs.archetype.mapper.RestItemMapper;
import com.nixs.archetype.request.CreateItemRequest;
import com.nixs.archetype.request.UpdateItemRequest;
import com.nixs.archetype.response.ItemResponse;
import com.nixs.archetype.service.ItemService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/items")
public class ItemController {

    private final RestItemMapper mapper;
    private final ItemService itemService;

    @GetMapping
    @PreAuthorize("hasAuthority('ITEM_READ')")
    public List<ItemResponse> getItems() {
        return itemService.findAll().stream()
            .map(mapper::toResponse)
            .toList();
    }

    @GetMapping("/{itemUuid}")
    @PreAuthorize("hasAuthority('ITEM_READ')")
    public ItemResponse getItem(@PathVariable UUID itemUuid) {
        Item item = itemService.findById(itemUuid);
        return mapper.toResponse(item);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ITEM_WRITE')")
    public ResponseEntity<ItemResponse> createItem(
        @Validated @RequestBody CreateItemRequest request) {
        Item item = mapper.from(request);
        Item createdItem = itemService.create(item);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(mapper.toResponse(createdItem));
    }

    @PutMapping("/{itemUuid}")
    @PreAuthorize("hasAuthority('ITEM_WRITE')")
    public ResponseEntity<ItemResponse> replaceItem(@PathVariable UUID itemUuid,
        @Validated @RequestBody UpdateItemRequest request) {
        Item item = mapper.from(itemUuid, request);
        Item savedItem = itemService.update(item);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(mapper.toResponse(savedItem));
    }

    @PatchMapping("/{itemUuid}")
    @PreAuthorize("hasAuthority('ITEM_WRITE')")
    public ResponseEntity<ItemResponse> replaceItem(@PathVariable UUID itemUuid,
        @RequestBody JsonMergePatch request) {
        Item savedItem = itemService.patch(itemUuid, request);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(mapper.toResponse(savedItem));
    }

    @DeleteMapping("/{itemUuid}")
    @PreAuthorize("hasAuthority('ITEM_DELETE')")
    public ResponseEntity<Object> deleteItem(@PathVariable UUID itemUuid) {
        itemService.deleteItem(itemUuid);
        return ResponseEntity.noContent().build();
    }
}

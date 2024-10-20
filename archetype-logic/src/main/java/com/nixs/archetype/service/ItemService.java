package com.nixs.archetype.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.nixs.archetype.dto.Item;
import com.nixs.archetype.exception.ItemPatchException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemPersistenceService itemPersistenceService;
    private final ObjectMapper objectMapper;

    public List<Item> findAll() {
        return itemPersistenceService.findAll();
    }

    public Item findById(UUID uuid) {
        return itemPersistenceService.getById(uuid);
    }

    public Item create(Item item) {
        Item created = itemPersistenceService.save(item);
        return created;
    }

    public Item update(Item item) {
        return itemPersistenceService.save(item);
    }

    public Item patch(UUID uuid, JsonMergePatch request) {
        Item original = itemPersistenceService.getById(uuid);
        Item itemToSave;
        try {
            JsonNode origJsonNode = objectMapper.convertValue(original, JsonNode.class);
            JsonNode patched = request.apply(origJsonNode);
            itemToSave = objectMapper.treeToValue(patched, Item.class);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new ItemPatchException(uuid, e);
        }
        return itemPersistenceService.save(itemToSave);
    }

    public void deleteItem(UUID uuid) {
        itemPersistenceService.deleteById(uuid);
    }
}

package com.nixs.archetype.service;

import com.nixs.archetype.dto.Item;
import com.nixs.archetype.entity.ItemEntity;
import com.nixs.archetype.exception.ItemAlreadyExistsException;
import com.nixs.archetype.exception.ItemNotFoundException;
import com.nixs.archetype.mapper.ItemEntityMapper;
import com.nixs.archetype.repository.ItemRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemPersistenceService {

    private final ItemRepository itemRepository;
    private final ItemEntityMapper itemEntityMapper;

    public List<Item> findAll() {
        return itemRepository.findAll().stream()
            .map(itemEntityMapper::toDto)
            .toList();
    }

    public Item getById(UUID uuid) {
        return itemRepository.findById(uuid)
            .map(itemEntityMapper::toDto)
            .orElseThrow(() -> new ItemNotFoundException(uuid));
    }

    @SuppressWarnings("unused")
    public Optional<Item> findById(UUID uuid) {
        return itemRepository.findById(uuid)
            .map(itemEntityMapper::toDto);
    }

    public Item save(Item item) {
        ItemEntity entity = itemEntityMapper.from(item);
        ItemEntity savedEntity;
        try {
            savedEntity = itemRepository.save(entity);
        } catch (DataIntegrityViolationException e) {
            throw new ItemAlreadyExistsException(item.getTitle());
        }
        return itemEntityMapper.toDto(savedEntity);
    }

    public void deleteById(UUID uuid) {
        itemRepository.deleteById(uuid);
    }
}

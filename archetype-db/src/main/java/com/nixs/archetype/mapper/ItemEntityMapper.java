package com.nixs.archetype.mapper;

import com.nixs.archetype.dto.Item;
import com.nixs.archetype.entity.ItemEntity;
import org.springframework.stereotype.Service;

@Service
public class ItemEntityMapper {

    public ItemEntity from(Item item) {
        return ItemEntity.builder()
            .uuid(item.getUuid())
            .title(item.getTitle())
            .length(item.getLength())
            .height(item.getHeight())
            .weight(item.getWeight())
            .manufacturedDate(item.getManufacturedDate())
            .build();
    }

    public Item toDto(ItemEntity item) {
        return Item.builder()
            .uuid(item.getUuid())
            .title(item.getTitle())
            .length(item.getLength())
            .height(item.getHeight())
            .weight(item.getWeight())
            .manufacturedDate(item.getManufacturedDate())
            .build();
    }
}

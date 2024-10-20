package com.nixs.archetype.mapper;

import com.nixs.archetype.dto.Item;
import com.nixs.archetype.request.CreateItemRequest;
import com.nixs.archetype.request.UpdateItemRequest;
import com.nixs.archetype.response.ItemResponse;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class RestItemMapper {

    public Item from(CreateItemRequest request) {
        return Item.builder()
            .title(request.getTitle())
            .length(request.getLength())
            .height(request.getHeight())
            .weight(request.getWeight())
            .manufacturedDate(request.getManufacturedDate())
            .build();
    }

    public Item from(UUID uuid, UpdateItemRequest request) {
        return Item.builder()
            .uuid(uuid)
            .title(request.getTitle())
            .length(request.getLength())
            .height(request.getHeight())
            .weight(request.getWeight())
            .manufacturedDate(request.getManufacturedDate())
            .build();
    }

    public ItemResponse toResponse(Item item) {
        return ItemResponse.builder()
            .uuid(item.getUuid())
            .title(item.getTitle())
            .length(item.getLength())
            .height(item.getHeight())
            .weight(item.getWeight())
            .manufacturedDate(item.getManufacturedDate())
            .build();
    }

}

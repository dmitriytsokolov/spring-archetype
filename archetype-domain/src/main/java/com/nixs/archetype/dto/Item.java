package com.nixs.archetype.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Item {

    UUID uuid;
    String title;
    int length;
    int height;
    int weight;
    Instant manufacturedDate;

}

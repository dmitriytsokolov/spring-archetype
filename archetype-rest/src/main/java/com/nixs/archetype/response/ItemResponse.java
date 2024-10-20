package com.nixs.archetype.response;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor
public class ItemResponse {

    UUID uuid;
    String title;
    int length;
    int height;
    int weight;
    Instant manufacturedDate;
}

package com.nixs.archetype.request;

import java.time.Instant;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdateItemRequest {

    String title;
    int length;
    int height;
    int weight;
    Instant manufacturedDate;
}

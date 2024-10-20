package com.nixs.archetype.event;

import java.time.Instant;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateItemEvent implements Event {

    String title;
    int length;
    int height;
    int weight;
    Instant manufacturedDate;
}

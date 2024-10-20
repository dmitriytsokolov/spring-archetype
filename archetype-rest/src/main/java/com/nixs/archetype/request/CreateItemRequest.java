package com.nixs.archetype.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.Instant;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateItemRequest {

    @NotEmpty
    String title;
    @PositiveOrZero
    int length;
    @PositiveOrZero
    int height;
    @PositiveOrZero
    int weight;
    @PastOrPresent
    Instant manufacturedDate;
}

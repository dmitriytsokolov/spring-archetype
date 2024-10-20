package com.nixs.archetype.event;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateItemNotificationEvent implements Event {

    UUID uuid;
    String title;
}

package com.nixs.archetype.exception;

import java.util.UUID;

public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(UUID itemUuid) {
        super("Can't find item with uuid='%s'".formatted(itemUuid));
    }
}

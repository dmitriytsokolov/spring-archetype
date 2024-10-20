package com.nixs.archetype.exception;

import java.util.UUID;

public class ItemPatchException extends RuntimeException {

    public ItemPatchException(UUID itemUuid, Throwable cause) {
        super("Exception while patching item with uuid='%s'".formatted(itemUuid), cause);
    }
}

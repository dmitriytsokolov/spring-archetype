package com.nixs.archetype.exception;

public class ItemAlreadyExistsException extends RuntimeException {

    public ItemAlreadyExistsException(String itemName) {
        super("Item with name='%s' already exists".formatted(itemName));
    }
}

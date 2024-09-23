package org.dreamdevwork.shop.exceptions;

import lombok.AllArgsConstructor;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String message) {
        super(message);
    }
}

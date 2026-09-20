package com.example.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public final class CartDtos {
    private CartDtos() {
    }

    public record AddItemRequest(@NotNull Long bookId, @Min(1) int quantity) {
    }

    public record UpdateItemRequest(@Min(1) int quantity) {
    }

    public record CartItemResponse(Long itemId, Long bookId, String title, int quantity, BigDecimal unitPrice,
                                   BigDecimal subtotal) {
    }

    public record CartResponse(List<CartItemResponse> items, BigDecimal total) {
    }
}

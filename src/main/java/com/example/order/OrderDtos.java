package com.example.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public final class OrderDtos {
    private OrderDtos() {
    }

    public record OrderItemResponse(Long bookId, String title, int quantity, BigDecimal unitPrice,
                                    BigDecimal subtotal) {
    }

    public record OrderResponse(Long orderId, BigDecimal total, OrderStatus status, LocalDateTime createdAt,
                                List<OrderItemResponse> items) {
    }
}

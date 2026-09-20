package com.example.order;

import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.common.IdempotencyService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;
    private final IdempotencyService idempotency;

    public OrderController(OrderService s, IdempotencyService i) {
        service = s;
        idempotency = i;
    }

    @PostMapping
    public ResponseEntity<OrderDtos.OrderResponse> checkout(Authentication a, @RequestHeader("Idempotency-Key") String key) {
        var existing = idempotency.get(a.getName(), key);
        if (existing != null) return ResponseEntity.status(HttpStatus.CREATED).body(existing);
        var created = service.checkout(a.getName());
        idempotency.put(a.getName(), key, created);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<OrderDtos.OrderResponse> history(Authentication a) {
        return service.history(a.getName());
    }
}

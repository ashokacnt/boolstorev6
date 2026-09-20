package com.example.cart;

import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService service;

    public CartController(CartService s) {
        service = s;
    }

    @GetMapping
    public CartDtos.CartResponse get(Authentication a) {
        return service.get(a.getName());
    }

    @PostMapping("/items")
    public ResponseEntity<CartDtos.CartResponse> add(Authentication a, @Valid @RequestBody CartDtos.AddItemRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.add(a.getName(), r));
    }

    @PutMapping("/items/{itemId}")
    public CartDtos.CartResponse update(Authentication a, @PathVariable Long itemId, @Valid @RequestBody CartDtos.UpdateItemRequest r) {
        return service.update(a.getName(), itemId, r);
    }

    @DeleteMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(Authentication a, @PathVariable Long itemId) {
        service.remove(a.getName(), itemId);
    }
}

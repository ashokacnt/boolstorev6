package com.example.cart;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserUsernameOrderById(String username);

    Optional<CartItem> findByIdAndUserUsername(Long id, String username);

    Optional<CartItem> findByUserUsernameAndBookId(String username, Long bookId);

    void deleteByUserUsername(String username);
}

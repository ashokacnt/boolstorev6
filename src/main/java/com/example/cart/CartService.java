package com.example.cart;

import java.math.BigDecimal;
import java.util.*;

import com.example.auth.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.auth.UserRepository;
import com.example.book.*;
import com.example.common.*;

@Service
public class CartService {
    private final CartItemRepository carts;
    private final BookRepository books;
    private final UserRepository users;

    public CartService(CartItemRepository c, BookRepository b, UserRepository u) {
        carts = c;
        books = b;
        users = u;
    }

    public CartDtos.CartResponse get(String username) {
        return dto(carts.findByUserUsernameOrderById(username));
    }

    @Transactional
    public CartDtos.CartResponse add(String username, CartDtos.AddItemRequest r) {
        if (r.quantity() < 1) throw new BadRequestException("Quantity must be greater than zero");
        User user = users.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Book book = books.findById(r.bookId()).orElseThrow(() -> new ResourceNotFoundException("Book not found: " + r.bookId()));
        if (!book.isActive()) throw new BadRequestException("Book is not available");
        var existing = carts.findByUserUsernameAndBookId(username, r.bookId());
        int q = existing.map(x -> x.getQuantity() + r.quantity()).orElse(r.quantity());
        if (q > book.getStockQuantity()) throw new ConflictException("Requested quantity exceeds available stock");
        if (existing.isPresent()) existing.get().setQuantity(q);
        else carts.save(new CartItem(user, book, q));
        return get(username);
    }

    @Transactional
    public CartDtos.CartResponse update(String username, Long id, CartDtos.UpdateItemRequest r) {
        if (r.quantity() < 1) throw new BadRequestException("Quantity must be greater than zero");
        CartItem i = carts.findByIdAndUserUsername(id, username).orElseThrow(() -> new ResourceNotFoundException("Cart item not found: " + id));
        if (r.quantity() > i.getBook().getStockQuantity())
            throw new ConflictException("Requested quantity exceeds available stock");
        i.setQuantity(r.quantity());
        return get(username);
    }

    @Transactional
    public void remove(String username, Long id) {
        CartItem i = carts.findByIdAndUserUsername(id, username).orElseThrow(() -> new ResourceNotFoundException("Cart item not found: " + id));
        carts.delete(i);
    }

    private CartDtos.CartResponse dto(List<CartItem> items) {
        var out = items.stream().map(i -> new CartDtos.CartItemResponse(i.getId(), i.getBook().getId(), i.getBook().getTitle(), i.getQuantity(), i.getBook().getPrice(), i.getBook().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))).toList();
        BigDecimal total = out.stream().map(CartDtos.CartItemResponse::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CartDtos.CartResponse(out, total);
    }
}

package com.example.order;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.auth.UserRepository;
import com.example.cart.*;
import com.example.common.*;

@Service
public class OrderService {
    private final CustomerOrderRepository orders;
    private final CartItemRepository carts;
    private final UserRepository users;

    public OrderService(CustomerOrderRepository o, CartItemRepository c, UserRepository u) {
        orders = o;
        carts = c;
        users = u;
    }

    @Transactional
    public OrderDtos.OrderResponse checkout(String username) {
        var user = users.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        var items = carts.findByUserUsernameOrderById(username);
        if (items.isEmpty()) throw new BadRequestException("Cannot checkout an empty cart");
        BigDecimal total = BigDecimal.ZERO;
        for (var i : items) {
            if (!i.getBook().isActive())
                throw new ConflictException("Book is not available: " + i.getBook().getTitle());
            if (i.getQuantity() > i.getBook().getStockQuantity())
                throw new ConflictException("Insufficient stock for: " + i.getBook().getTitle());
            total = total.add(i.getBook().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())));
        }
        CustomerOrder order = new CustomerOrder(user, total);
        for (var i : items) {
            i.getBook().setStockQuantity(i.getBook().getStockQuantity() - i.getQuantity());
            order.addItem(new OrderItem(i.getBook(), i.getQuantity(), i.getBook().getPrice()));
        }
        CustomerOrder saved = orders.save(order);
        carts.deleteAll(items);
        return dto(saved);
    }

    public List<OrderDtos.OrderResponse> history(String username) {
        return orders.findByUserUsernameOrderByCreatedAtDesc(username).stream().map(this::dto).toList();
    }

    private OrderDtos.OrderResponse dto(CustomerOrder o) {
        var items = o.getItems().stream().map(i -> new OrderDtos.OrderItemResponse(i.getBook().getId(), i.getBook().getTitle(), i.getQuantity(), i.getUnitPrice(), i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))).toList();
        return new OrderDtos.OrderResponse(o.getId(), o.getTotal(), o.getStatus(), o.getCreatedAt(), items);
    }
}

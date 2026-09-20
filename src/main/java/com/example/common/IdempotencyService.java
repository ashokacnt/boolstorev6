package com.example.common;

import java.util.concurrent.*;

import org.springframework.stereotype.Service;
import com.example.order.OrderDtos;

@Service
public class IdempotencyService {
    private final ConcurrentMap<String, Entry> entries = new ConcurrentHashMap<>();

    public OrderDtos.OrderResponse get(String user, String key) {
        Entry e = entries.get(key);
        if (e == null) return null;
        if (!e.user.equals(user))
            throw new ConflictException("Idempotency-Key is already associated with another user");
        return e.response;
    }

    public void put(String user, String key, OrderDtos.OrderResponse response) {
        entries.putIfAbsent(key, new Entry(user, response));
    }

    private record Entry(String user, OrderDtos.OrderResponse response) {
    }
}

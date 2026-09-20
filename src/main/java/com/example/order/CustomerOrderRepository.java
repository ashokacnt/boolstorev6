package com.example.order;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByUserUsernameOrderByCreatedAtDesc(String username);

    Optional<CustomerOrder> findByIdAndUserUsername(Long id, String username);
}

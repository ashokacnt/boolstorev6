package com.example.order;

import com.example.book.Book;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CustomerOrder order;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Book book;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    public OrderItem() {
    }

    public OrderItem(Book b, int q, BigDecimal p) {
        book = b;
        quantity = q;
        unitPrice = p;
    }

    public void setOrder(CustomerOrder o) {
        order = o;
    }

    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
}

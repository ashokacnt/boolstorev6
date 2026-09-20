package com.example.book;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "books", uniqueConstraints = @UniqueConstraint(name = "uk_book_isbn", columnNames = "isbn"))
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private String isbn;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;
    @Column(nullable = false)
    private int stockQuantity;
    @Column(nullable = false)
    private boolean active = true;

    public Book() {
    }

    public Book(String t, String a, String i, BigDecimal p, int s) {
        title = t;
        author = a;
        isbn = i;
        price = p;
        stockQuantity = s;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int v) {
        stockQuantity = v;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean v) {
        active = v;
    }
}

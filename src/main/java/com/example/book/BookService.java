package com.example.book;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.common.*;

@Service
public class BookService {
    private final BookRepository repo;

    public BookService(BookRepository r) {
        repo = r;
    }

    public List<BookDtos.BookResponse> findAll() {
        return repo.findAll().stream().filter(Book::isActive).map(this::toDto).toList();
    }

    @Transactional
    public BookDtos.BookResponse add(BookDtos.CreateBookRequest r) {
        if (repo.findByIsbn(r.isbn()).isPresent()) throw new ConflictException("Book ISBN already exists: " + r.isbn());
        return toDto(repo.save(new Book(r.title(), r.author(), r.isbn(), r.price(), r.stockQuantity())));
    }

    public Book get(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found: " + id));
    }

    public BookDtos.BookResponse toDto(Book b) {
        return new BookDtos.BookResponse(b.getId(), b.getTitle(), b.getAuthor(), b.getIsbn(), b.getPrice(), b.getStockQuantity(), b.isActive());
    }
}

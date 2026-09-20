package com.example.book;

import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService service;

    public BookController(BookService s) {
        service = s;
    }

    @GetMapping
    public List<BookDtos.BookResponse> list() {
        return service.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDtos.BookResponse> add(@Valid @RequestBody BookDtos.CreateBookRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.add(req));
    }
}

package com.example.book;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public final class BookDtos {
    private BookDtos() {
    }

    public record CreateBookRequest(@NotBlank String title, @NotBlank String author, @NotBlank String isbn,
                                    @NotNull @DecimalMin("0.01") BigDecimal price, @Min(0) int stockQuantity) {
    }

    public record BookResponse(Long id, String title, String author, String isbn, BigDecimal price, int stockQuantity,
                               boolean active) {
    }
}

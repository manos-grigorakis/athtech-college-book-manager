package gr.athtech.app.bookmanager.transfer.book;

import gr.athtech.app.bookmanager.transfer.author.AuthorSummary;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BookResponse(
        Long id,
        String title,
        String category,
        LocalDate publishDate,
        Integer numberOfPages,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        AuthorSummary author
) {}

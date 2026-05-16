package gr.athtech.app.bookmanager.transfer.book;

import java.time.LocalDate;

public record BookSearchRequest(
        String title,
        String category,
        LocalDate publishDate,
        Integer numberOfPages,
        String authorName
) {}

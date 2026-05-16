package gr.athtech.app.bookmanager.transfer.author;

import java.time.LocalDateTime;

public record AuthorResponse(
        Long id,
        String firstName,
        String lastName,
        String country,
        Integer numberOfPublishedBooks,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

package gr.athtech.app.bookmanager.mapper;

import gr.athtech.app.bookmanager.model.Author;
import gr.athtech.app.bookmanager.model.Book;
import gr.athtech.app.bookmanager.transfer.author.AuthorSummary;
import gr.athtech.app.bookmanager.transfer.book.BookRequest;
import gr.athtech.app.bookmanager.transfer.book.BookResponse;

public class BookMapper {
    // DTO -> Entity
    public static Book toEntity(BookRequest dto) {
        return Book.builder()
                .title(dto.title())
                .category(dto.category())
                .publishDate(dto.publishDate())
                .numberOfPages(dto.numberOfPages())
                .build();
    }

    // Entity -> Response
    public static BookResponse toResponse(Book book) {
        Author author = book.getAuthor();
        AuthorSummary authorSummary = new AuthorSummary(author.getId(), author.getFullName());

        return new BookResponse(book.getId(), book.getTitle(), book.getCategory(), book.getPublishDate(),
                                book.getNumberOfPages(), book.getCreatedAt(), book.getUpdatedAt(), authorSummary);
    }
}

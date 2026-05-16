package gr.athtech.app.bookmanager.mapper;

import gr.athtech.app.bookmanager.model.Author;
import gr.athtech.app.bookmanager.transfer.author.AuthorRequest;
import gr.athtech.app.bookmanager.transfer.author.AuthorResponse;

public class AuthorMapper {
    // DTO -> Entity
    public static Author toEntity(AuthorRequest dto) {
        return Author.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .country(dto.country())
                .build();
    }

    // Entity -> Response
    public static AuthorResponse toResponse(Author author) {
        return new AuthorResponse(author.getId(), author.getFirstName(), author.getLastName(), author.getCountry(),
                                  author.getNumberOfPublishedBooks(), author.getCreatedAt(),
                                  author.getUpdatedAt());
    }

    // Update Entity
    public static void updateAuthor(Author author, AuthorRequest dto) {
        author.setFirstName(dto.firstName());
        author.setLastName(dto.lastName());
        author.setCountry(dto.country());
    }
}

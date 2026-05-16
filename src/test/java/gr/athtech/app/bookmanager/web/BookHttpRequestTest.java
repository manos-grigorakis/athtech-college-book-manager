package gr.athtech.app.bookmanager.web;

import gr.athtech.app.bookmanager.model.Author;
import gr.athtech.app.bookmanager.model.Book;
import gr.athtech.app.bookmanager.transfer.author.AuthorRequest;
import gr.athtech.app.bookmanager.transfer.author.AuthorResponse;
import gr.athtech.app.bookmanager.transfer.book.BookRequest;
import gr.athtech.app.bookmanager.transfer.book.BookResponse;
import gr.athtech.app.bookmanager.transfer.common.ApiResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class BookHttpRequestTest extends HttpRequestTest {
    @Test
    public void getBookById_shouldGetBookById() {
        // Arrange
        ResponseEntity<ApiResponse<BookResponse>> createBookResponse = createInitialBookWithAuthor();
        Assertions.assertNotNull(createBookResponse.getBody());
        BookResponse createBookData = createBookResponse.getBody().getData();

        // Act
        ResponseEntity<ApiResponse<BookResponse>> response = getBookById(createBookData.id());
        Assertions.assertNotNull(response.getBody());
        BookResponse bookResponseData = response.getBody().getData();

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(bookResponseData.id()).isEqualTo(createBookData.id());
        assertThat(bookResponseData.title()).isEqualTo(createBookData.title());
        assertThat(bookResponseData.category()).isEqualTo(createBookData.category());
        assertThat(bookResponseData.numberOfPages()).isEqualTo(createBookData.numberOfPages());
    }

    @Test
    public void getBookById_shouldReturn404_whenBookNotFound() {
        // Arrange & Act
        ResponseEntity<ApiResponse<BookResponse>> response = getBookById(1000L);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void createBook_shouldCreateBook() {
        // Arrange
        ResponseEntity<ApiResponse<AuthorResponse>> authorResponse = createInitialAuthorAndGetResponse();
        Assertions.assertNotNull(authorResponse.getBody());
        Long authorId = authorResponse.getBody().getData().id();

        // Arrange Book & Act
        ResponseEntity<ApiResponse<BookResponse>> response = createInitialBookAndGetResponse(authorId);

        Assertions.assertNotNull(response.getBody());
        BookResponse bookResponse = response.getBody().getData();

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(bookResponse.author().id()).isEqualTo(authorId);
        assertThat(bookResponse.title()).isEqualTo("My Book");
        assertThat(bookResponse.category()).isEqualTo("Fantasy");
        assertThat(bookResponse.numberOfPages()).isEqualTo(105);
    }

    @Test
    public void createBookWithNonExistingAuthor_shouldReturn404() {
        // Arrange & Act
        ResponseEntity<ApiResponse<BookResponse>> response = createInitialBookAndGetResponse(1000L);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void deleteBookByIdWithNonExistingBook_shouldReturn404() {
        // Arrange
        String url = BASE_URL + "/books/" + 1000L;

        // Act
        ResponseEntity<ApiResponse<BookResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<ApiResponse<BookResponse>>() {}
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void deleteBookById_shouldDeleteBookByIdWithoutDeletingAuthor() {
        // Arrange
        ResponseEntity<ApiResponse<BookResponse>> createBookResponse = createInitialBookWithAuthor();
        Assertions.assertNotNull(createBookResponse.getBody());
        BookResponse createBookData = createBookResponse.getBody().getData();
        String url = BASE_URL + "/books/" + createBookData.id();

        // Act
        ResponseEntity<ApiResponse<BookResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<ApiResponse<BookResponse>>() {}
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // Assert Book is deleted
        ResponseEntity<ApiResponse<BookResponse>> getBookByIdResponse = getBookById(createBookData.id());
        assertThat(getBookByIdResponse.getStatusCode().value()).isEqualTo(HttpStatus.NOT_FOUND.value());

        // Assert author exists
        ResponseEntity<ApiResponse<AuthorResponse>> authorResponse = getAuthorById(createBookData.author().id());
        Assertions.assertNotNull(authorResponse.getBody());
        AuthorResponse authorData = authorResponse.getBody().getData();
        assertThat(authorResponse.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(authorData.id()).isEqualTo(createBookData.author().id());
    }


    /**
     * Creates a {@link Book} to be used in test methods with the following properties:
     * <ul>
     *     <li>{@code title} = {@code My Book}</li>
     *     <li>{@code category} = {@code Fantasy}</li>
     *     <li>{@code numberOfPages} = {@code 105}</li>
     *     <li>{@code publishDate} = {@code 2020-01-20}</li>
     *     <li>{@code authorId} = {@code authorId} (from params)</li>
     * </ul>
     * @param authorId The {@link Author} {@code id} to be associated with the {@link Book}
     * @return The response of the endpoint
     */
    private ResponseEntity<ApiResponse<BookResponse>> createInitialBookAndGetResponse(Long authorId) {
        // Arrange
        String url = BASE_URL + "/books";
        BookRequest bookRequest = new BookRequest("My Book", "Fantasy",
                                                  LocalDate.parse("2020-01-20"), 105, authorId);

        HttpEntity<BookRequest> request = new HttpEntity<>(bookRequest);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<ApiResponse<BookResponse>>() {
                }
        );
    }

    /**
     * Creates an {@link Author} using {@link #createInitialAuthorAndGetResponse()},
     * which will be associated to the {@link Book} that is created using {@link #createInitialBookAndGetResponse(Long)}
     * @return The response of the {@link #createInitialAuthorAndGetResponse()}
     */
    private ResponseEntity<ApiResponse<BookResponse>> createInitialBookWithAuthor() {
        // Arrange Author
        ResponseEntity<ApiResponse<AuthorResponse>> authorResponse = createInitialAuthorAndGetResponse();
        Long authorId = authorResponse.getBody().getData().id();

        // Arrange Book & Act
        return createInitialBookAndGetResponse(authorId);
    }

    /**
     * Finds an {@link Author} by the provided {@code authorId}
     * @param authorId The {@link Author} {@code id} used to find the {@link Author}
     * @return The response of the endpoint
     */
    private ResponseEntity<ApiResponse<AuthorResponse>> getAuthorById(Long authorId) {
        // Arrange
        String url =  BASE_URL + "/authors/" + authorId;

        // Act
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<AuthorResponse>>() {}
        );
    }

    /**
     * Creates an {@link Author} with the following properties:
     * <ul>
     *     <li>{@code firstName} = {@code John}</li>
     *     <li>{@code lastName} = {@code Doe}</li>
     *     <li>{@code country} = {@code Greece}</li>
     * </ul>
     * @return The response from the endpoint
     */
    private ResponseEntity<ApiResponse<AuthorResponse>> createInitialAuthorAndGetResponse() {
        // Arrange
        String url = BASE_URL + "/authors";
        AuthorRequest authorRequest = new AuthorRequest("John", "Doe", "Greece");
        HttpEntity<AuthorRequest> request = new HttpEntity<>(authorRequest);

        // Act
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<ApiResponse<AuthorResponse>>() {
                }
        );
    }

    /**
     * Finds a {@link Book} by the provided {@code bookId}
     * @param bookId The {@link Book} {@code id} used to find the {@link Book}
     * @return The response of the endpoint
     */
    private ResponseEntity<ApiResponse<BookResponse>> getBookById(Long bookId) {
        // Arrange
        String url = BASE_URL + "/books/" + bookId;

        // Act
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<BookResponse>>() {}
        );
    }
}

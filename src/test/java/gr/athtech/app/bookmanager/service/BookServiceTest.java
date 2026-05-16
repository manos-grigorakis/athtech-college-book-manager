package gr.athtech.app.bookmanager.service;

import gr.athtech.app.bookmanager.exception.ResourceNotFoundException;
import gr.athtech.app.bookmanager.model.Author;
import gr.athtech.app.bookmanager.model.Book;
import gr.athtech.app.bookmanager.repository.AuthorRepository;
import gr.athtech.app.bookmanager.repository.BookRepository;
import gr.athtech.app.bookmanager.transfer.book.BookRequest;
import gr.athtech.app.bookmanager.transfer.book.BookResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Author author;
    private Book book;
    private BookRequest createBookRequest;
    private BookRequest updateBookRequest;

    @BeforeEach
    public void setUp() {
        author = new Author();
        author.setId(1L);

        book = createBook();
        createBookRequest = createBookRequest("title");
        updateBookRequest = createBookRequest("title2");
    }

    @Test
    public void getAllBooks_shouldReturnListOfAllBooks() {
        // Arrange
        Book book1 = new Book();
        Book book2 = new Book();
        book1.setAuthor(author);
        book2.setAuthor(author);

        when(bookRepository.findAllBooksWithAuthors()).thenReturn(List.of(book1, book2));

        // Act
        List<BookResponse> response = bookService.getAll();

        // Assert
        assertEquals(2, response.size());
    }

    @Test
    public void getBookById_shouldReturnBookById() {
        // Arrange
        Book book = new Book();
        book.setTitle("title");
        book.setAuthor(author);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act
        BookResponse response = bookService.findEntityById(1L);

        // Assert
        assertEquals("title", response.title());
    }

    @Test
    public void getBookById_shouldThrowNotFoundException_whenBookNotFound() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Act && Assert
        assertThrows(ResourceNotFoundException.class, () -> bookService.findEntityById(1L));
    }

    @Test
    public void createBook_shouldCreateBook() {
        // Arrange
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookRequest request = new BookRequest("title", "category",
                                              LocalDate.of(2020, 10, 10),
                                              100, author.getId());

        // Act
        BookResponse response = bookService.createEntity(request);

        // Assert
        verify(bookRepository, times(1)).save(any(Book.class));
        assertEquals("title", response.title());
        assertEquals("category", response.category());
        assertEquals(LocalDate.of(2020, 10, 10), response.publishDate());
        assertEquals(100, response.numberOfPages());
        assertEquals(author.getId(), response.author().id());
    }

    @Test
    public void createBook_shouldThrowNotFoundException_whenAuthorNotFound() {
        // Arrange
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bookService.createEntity(createBookRequest));
    }

    @Test
    public void updateBookById_shouldUpdateBookById() {
        // Arrange
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // Act
        BookResponse response = bookService.updateEntityById(1L, updateBookRequest);

        // Assert
        verify(bookRepository, times(1)).save(any(Book.class));
        assertEquals("title2", response.title());
        assertEquals("category", response.category());
        assertEquals(LocalDate.of(2020, 10, 10), response.publishDate());
        assertEquals(100, response.numberOfPages());
        assertEquals(author.getId(), response.author().id());
    }

    @Test
    public void updateBookById_shouldThrowNotFoundException_whenBookNotFound() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Act && Assert
        assertThrows(ResourceNotFoundException.class, () -> bookService.updateEntityById(1L, updateBookRequest));
    }

    @Test
    public void updateBookById_shouldThrowNotFoundException_whenAuthorNotFound() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book()));
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act && Assert
        assertThrows(ResourceNotFoundException.class, () -> bookService.updateEntityById(1L, updateBookRequest));
    }

    @Test
    public void deleteBookById_shouldDeleteBookById() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book()));
        doNothing().when(bookRepository).deleteById(1L);

        // Act
        bookService.deleteEntityById(1L);

        // Assert
        verify(bookRepository).deleteById(1L);
    }

    @Test
    public void deleteBookById_shouldThrowNotFoundException_whenBookNotFound() {
        // Act && Assert
        assertThrows(ResourceNotFoundException.class, () -> bookService.deleteEntityById(1L));
    }

    @Test
    public void findCategoryDistribution_ShouldReturnListOfCategories() {
        // Arrange
        when(bookRepository.findCategoryDistribution()).thenReturn(List.of());

        // Act
        bookService.findCategoryDistribution();

        // Assert
        verify(bookRepository, times(1)).findCategoryDistribution();
    }

    @Test
    public void findAuthorTimeline_ShouldReturnListOfAuthorTimeline() {
        // Arrange
        when(bookRepository.findAuthorTimeline(1L)).thenReturn(List.of());

        // Act
        bookService.findAuthorTimeline(1L);

        // Assert
        verify(bookRepository, times(1)).findAuthorTimeline(1L);
    }

    @Test
    public void findGeographicDistribution_ShouldReturnListOfGeographicDistribution() {
        // Arrange
        when(bookRepository.findGeographicDistribution()).thenReturn(List.of());

        // Act
        bookService.findGeographicDistribution();

        // Assert
        verify(bookRepository, times(1)).findGeographicDistribution();
    }

    /**
     * Creates a {@link Book} to be used for testing purposes with {@link #author} assigned
     * @return The created {@link Book}
     */
    private Book createBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("title");
        book.setCategory("category");
        book.setPublishDate(LocalDate.of(2020, 10, 10));
        book.setNumberOfPages(100);
        book.setAuthor(author);

        return book;
    }

    /**
     * Creates a {@link BookRequest} to be used for create and update requests
     * @param title The {@link Book#title}
     * @return The {@link BookRequest}
     */
    private BookRequest createBookRequest(String title) {
        return new BookRequest(title, "category", LocalDate.of(2020, 10, 10),
                               100, author.getId());
    }
}

package gr.athtech.app.bookmanager.service;

import gr.athtech.app.bookmanager.exception.ConflictException;
import gr.athtech.app.bookmanager.exception.ResourceNotFoundException;
import gr.athtech.app.bookmanager.model.Author;
import gr.athtech.app.bookmanager.repository.AuthorRepository;
import gr.athtech.app.bookmanager.repository.BookRepository;
import gr.athtech.app.bookmanager.transfer.author.AuthorRequest;
import gr.athtech.app.bookmanager.transfer.author.AuthorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    public void getAllAuthors_shouldReturnListOfAllAuthors() {
        // Arrange
        when(authorRepository.findAll()).thenReturn(List.of(new Author(), new Author()));

        // Act
        List<AuthorResponse> response = authorService.getAll();

        // Assert
        assertEquals(2, response.size());
    }

    @Test
    public void getAuthorId_shouldReturnAuthorById() {
        // Arrange
        Author author = new Author();
        author.setFirstName("John");
        author.setLastName("Doe");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        // Act
        AuthorResponse response = authorService.findEntityById(1L);

        // Assert
        assertEquals("John", response.firstName());
        assertEquals("Doe", response.lastName());
    }

    @Test
    public void getAuthorById_shouldThrowNotFoundException_whenAuthorNotFound() {
        // Arrange
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act && Assert
        assertThrows(ResourceNotFoundException.class, () -> authorService.findEntityById(1L));
    }

    @Test
    public void createAuthor_shouldCreateAuthor() {
        // Arrange
        Author author = new Author();
        author.setId(1L);
        author.setFirstName("John");
        author.setLastName("Doe");
        author.setCountry("Greece");

        when(authorRepository.save(any(Author.class))).thenReturn(author);

        // Act
        AuthorResponse response = authorService.createEntity(new AuthorRequest("John", "Doe", "Greece"));

        // Assert
        verify(authorRepository, times(1)).save(any(Author.class));
        assertEquals("John", response.firstName());
        assertEquals("Doe", response.lastName());
        assertEquals("Greece", response.country());
    }

    @Test
    public void updateAuthorById_shouldUpdateAuthor() {
        // Arrange
        Author author = new Author();
        author.setId(1L);
        author.setFirstName("John");
        author.setLastName("Doe");
        author.setCountry("Greece");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        // Act
        AuthorResponse response = authorService.updateEntityById(
                1L, new AuthorRequest("Alice", "Doe", "Greece"));

        // Assert
        verify(authorRepository, times(1)).save(any(Author.class));
        assertEquals("Alice", response.firstName());
        assertEquals("Doe", response.lastName());
        assertEquals("Greece", response.country());
    }

    @Test
    public void updateAuthorById_shouldThrowNotFoundException_whenAuthorNotFound() {
        // Arrange
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act && Assert
        assertThrows(ResourceNotFoundException.class, () -> authorService.updateEntityById(
                1L, new AuthorRequest("John", "Doe", "Greece")));
    }

    @Test
    public void deleteAuthorById_shouldDeleteAuthor() {
        // Arrange
        when(authorRepository.findById(1L)).thenReturn(Optional.of(new Author()));
        when(bookRepository.existsByAuthorId(1L)).thenReturn(false);
        doNothing().when(authorRepository).deleteById(1L);

        // Act
        authorService.deleteEntityById(1L);

        // Assert
        verify(authorRepository).deleteById(1L);
    }

    @Test
    public void deleteAuthorById_shouldThrowResourceNotFoundException_whenAuthorNotFound() {
        // Act && Assert
        assertThrows(ResourceNotFoundException.class, () -> authorService.deleteEntityById(1L));
    }

    @Test
    public void deleteAuthorById_shouldThrowConflictException_whenBooksExist() {
        // Arrange
        when(authorRepository.findById(1L)).thenReturn(Optional.of(new Author()));
        when(bookRepository.existsByAuthorId(1L)).thenReturn(true);

        // Act && Assert
        assertThrows(ConflictException.class, () -> authorService.deleteEntityById(1L));
    }
}

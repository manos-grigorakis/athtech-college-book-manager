package gr.athtech.app.bookmanager.service;

import gr.athtech.app.bookmanager.mapper.BookMapper;
import gr.athtech.app.bookmanager.model.Author;
import gr.athtech.app.bookmanager.model.Book;
import gr.athtech.app.bookmanager.repository.AuthorRepository;
import gr.athtech.app.bookmanager.repository.BookRepository;
import gr.athtech.app.bookmanager.specs.BookSpecs;
import gr.athtech.app.bookmanager.transfer.book.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl extends BaseServiceImpl<Book, BookRequest, BookResponse> implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository) {
        super(bookRepository);
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    protected BookResponse mapToResponse(Book book) {
        return BookMapper.toResponse(book);
    }

    @Override
    protected Book mapToEntity(BookRequest dto) {
        return BookMapper.toEntity(dto);
    }

    @Override
    protected void updateEntity(Book book, BookRequest dto) {
        Author author = findOrThrow(authorRepository, dto.authorId(), "Author");

        book.setTitle(dto.title());
        book.setCategory(dto.category());
        book.setPublishDate(dto.publishDate());
        book.setNumberOfPages(dto.numberOfPages());
        book.setAuthor(author);
    }

    @Cacheable(value = "books")
    @Override
    public List<BookResponse> getAll() {
        return bookRepository.findAllBooksWithAuthors().stream().map(BookMapper::toResponse).toList();
    }

    @Cacheable(value = "bookId", key = "#id")
    @Override
    public BookResponse findEntityById(Long id) {
        return super.findEntityById(id);
    }

    @Caching(evict = {
            @CacheEvict(value = "books", allEntries = true),
            @CacheEvict(value = "booksCategoryDistribution", allEntries = true),
            @CacheEvict(value = "booksGeographicDistribution", allEntries = true),
            @CacheEvict(value = "booksAuthorTimeline", allEntries = true)
    })
    @Override
    public BookResponse createEntity(BookRequest dto) {
        Author author = findOrThrow(authorRepository, dto.authorId(), "Author");

        Book book = mapToEntity(dto);
        book.setAuthor(author);
        Book savedBook = repository.save(book);
        log.info("Created Book with id {}", savedBook.getId());
        return mapToResponse(savedBook);
    }

    @Caching(evict = {
            @CacheEvict(value = "books", allEntries = true),
            @CacheEvict(value = "booksCategoryDistribution", allEntries = true),
            @CacheEvict(value = "booksGeographicDistribution", allEntries = true),
            @CacheEvict(value = "bookId", key = "#id"),
            @CacheEvict(value = "booksAuthorTimeline", allEntries = true)
    })
    @Override
    public BookResponse updateEntityById(Long id, BookRequest dto) {
        return super.updateEntityById(id, dto);
    }

    @Caching(evict = {
            @CacheEvict(value = "books", allEntries = true),
            @CacheEvict(value = "booksCategoryDistribution", allEntries = true),
            @CacheEvict(value = "booksGeographicDistribution", allEntries = true),
            @CacheEvict(value = "bookId", key = "#id"),
            @CacheEvict(value = "booksAuthorTimeline", allEntries = true)
    })
    @Override
    public void deleteEntityById(Long id) {
        super.deleteEntityById(id);
    }

    @Override
    protected String getEntityName() {
        return "Book";
    }

    @Override
    public List<BookResponse> searchBooks(BookSearchRequest searchRequest) {
        Specification<Book> spec = Specification.allOf();

        if (searchRequest.title() != null) {
            spec = spec.and(BookSpecs.likeTitle(searchRequest.title()));
        }

        if (searchRequest.category() != null) {
            spec = spec.and(BookSpecs.likeCategory(searchRequest.category()));
        }

        if (searchRequest.publishDate() != null) {
            spec = spec.and(BookSpecs.equalPublishDate(searchRequest.publishDate()));
        }

        if (searchRequest.numberOfPages() != null) {
            spec = spec.and(BookSpecs.equalNumberOfPages(searchRequest.numberOfPages()));
        }

        if (searchRequest.authorName() != null) {
            spec = spec.and(BookSpecs.likeAuthorName(searchRequest.authorName()));
        }

        List<Book> books = bookRepository.findAll(spec);

        return books.stream().map(BookMapper::toResponse).toList();
    }

    @Cacheable(value = "booksCategoryDistribution")
    @Override
    public List<CategoryDistributionResponse> findCategoryDistribution() {
        return bookRepository.findCategoryDistribution();
    }

    @Cacheable(value = "booksAuthorTimeline", key = "#authorId")
    @Override
    public List<AuthorTimelineResponse> findAuthorTimeline(Long authorId) {
        return bookRepository.findAuthorTimeline(authorId);
    }

    @Cacheable(value = "booksGeographicDistribution")
    @Override
    public List<GeographicDistributionResponse> findGeographicDistribution() {
        return bookRepository.findGeographicDistribution();
    }
}

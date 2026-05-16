package gr.athtech.app.bookmanager.service;

import gr.athtech.app.bookmanager.exception.ConflictException;
import gr.athtech.app.bookmanager.mapper.AuthorMapper;
import gr.athtech.app.bookmanager.model.Author;
import gr.athtech.app.bookmanager.repository.AuthorRepository;
import gr.athtech.app.bookmanager.repository.BookRepository;
import gr.athtech.app.bookmanager.transfer.author.AuthorRequest;
import gr.athtech.app.bookmanager.transfer.author.AuthorResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl extends BaseServiceImpl<Author, AuthorRequest, AuthorResponse> implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    protected AuthorServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository) {
        super(authorRepository);
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    protected AuthorResponse mapToResponse(Author author) {
        return AuthorMapper.toResponse(author);
    }

    @Override
    protected Author mapToEntity(AuthorRequest dto) {
        return AuthorMapper.toEntity(dto);
    }

    @Override
    protected void updateEntity(Author author, AuthorRequest dto) {
        AuthorMapper.updateAuthor(author, dto);
    }

    @Override
    protected String getEntityName() {
        return "Author";
    }

    @Cacheable("authors")
    @Override
    public List<AuthorResponse> getAll() {
        return super.getAll();
    }

    @Cacheable(value = "authorId", key = "#id")
    @Override
    public AuthorResponse findEntityById(Long id) {
        return super.findEntityById(id);
    }

    @CacheEvict(value = "authors", allEntries = true)
    @Override
    public AuthorResponse createEntity(AuthorRequest dto) {
        return super.createEntity(dto);
    }

    @Caching(evict = {
            @CacheEvict(value = "authors", allEntries = true),
            @CacheEvict(value = "authorId", key = "#id")
    })
    @Override
    public AuthorResponse updateEntityById(Long id, AuthorRequest dto) {
        return super.updateEntityById(id, dto);
    }

    @Caching(evict = {
            @CacheEvict(value = "authors", allEntries = true),
            @CacheEvict(value = "authorId", key = "#id")
    })
    @Override
    public void deleteEntityById(Long id) {
        findOrThrow(authorRepository, id, getEntityName());

        if (bookRepository.existsByAuthorId(id)) {
           throw new ConflictException("Cannot delete author with existing books");
        }

        authorRepository.deleteById(id);
        log.info("Deleted {} with id {}", getEntityName(), id);
    }
}

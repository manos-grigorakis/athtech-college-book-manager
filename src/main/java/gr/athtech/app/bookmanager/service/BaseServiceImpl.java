package gr.athtech.app.bookmanager.service;

import gr.athtech.app.bookmanager.exception.ResourceNotFoundException;
import gr.athtech.app.bookmanager.model.BaseModel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Slf4j(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseServiceImpl<T extends BaseModel, D, R> implements BaseService<R, Long, D> {
    protected final JpaRepository<T, Long> repository;

    /**
     * Implement the method to convert the raw entity to a response (e.g. using a mapper)
     * @param entity The entity to be converted to a response
     * @return The object of the entity's response
     */
    protected abstract R mapToResponse(T entity);

    /**
     * Implement the method to map the DTO to the entity
     * @param dto The requested DTO
     * @return The object of the entity
     */
    protected abstract T mapToEntity(D dto);

    /**
     * Implement the method to update the entity using the provided request DTO object
     * @param entity The entity to be updated
     * @param dto The request DTO containing the update payload
     */
    protected abstract void updateEntity(T entity, D dto);

    /**
     * Used for logging purposes in the basic CRUD operations.
     * <p>Override and return the proper entity class name.</p>
     * @return Default {@code Entity}
     */
    protected String getEntityName() {return "Entity";}

    @Override
    public List<R> getAll() {
        return repository.findAll().stream().map(this::mapToResponse).toList();
    }

    /**
     * Searches in the repository for an entity with the provided ID
     * @param id The ID of the entity
     * @return The entity object converted to response using {@link #mapToResponse(BaseModel)}
     * @throw {@link ResourceNotFoundException} when entity doesn't exist
     */
    @Override
    public R findEntityById(Long id) {
        T entity = findOrThrow(repository, id, getEntityName());
        return mapToResponse(entity);
    }

    /**
     * Creates an entity and persist it in the database repository
     * @param dto The requested entity DTO
     * @return The entity mapped to response using {@link #mapToResponse(BaseModel)}
     */
    @Override
    public R createEntity(D dto) {
        T entity = mapToEntity(dto);
        T savedEntity = repository.save(entity);

        log.info("Created {} with id {}", getEntityName(), savedEntity.getId());
        return mapToResponse(savedEntity);
    }

    /**
     * Update an entity
     * @param id The ID of the entity to be updated
     * @param dto The request DTO to update the entity
     * @return The entity converted to response using {@link #mapToResponse(BaseModel)}
     * @throw {@link ResourceNotFoundException} when entity doesn't exist
     */
    @Override
    public R updateEntityById(Long id, D dto) {
        T entity = findOrThrow(repository, id, getEntityName());
        updateEntity(entity, dto);

        T updatedEntity = repository.save(entity);
        log.info("Updated {} with id {}", getEntityName(), updatedEntity.getId());
        return mapToResponse(updatedEntity);
    }

    /**
     * Searches for the provided entity in the repository and delete if exists
     * @param id The ID of the entity to be deleted
     * @throw {@link ResourceNotFoundException} when entity doesn't exist
     */
    @Override
    public void deleteEntityById(Long id) {
        findOrThrow(repository, id, getEntityName());

        repository.deleteById(id);
        log.info("Deleted {} with id {}", getEntityName(), id);
    }

    /**
     * Searches in the provided repository for an entity by the provided ID.
     * <p>If the entity exists, it will be returned. Otherwise {@link ResourceNotFoundException} will be thrown.</p>
     * @param repo The repository of the entity to be queried
     * @param id The ID of the entity to be searched in the repository
     * @param entityName The entity class name used for logging
     * @param <E> The entity object
     * @return The object {@code E} if exists
     * @throw {@link ResourceNotFoundException} when entity doesn't exist
     */
    protected <E> E findOrThrow(JpaRepository<E, Long> repo, Long id, String entityName) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "No " + entityName + " found with id " + id));
    }
}

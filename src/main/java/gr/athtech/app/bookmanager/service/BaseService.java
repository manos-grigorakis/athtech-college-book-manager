package gr.athtech.app.bookmanager.service;

import java.util.List;

/**
 * Base service for basic CRUD operations
 * @param <R> Response
 * @param <ID> Long
 * @param <D> DTO to createEntity the entity
 */
public interface BaseService<R, ID, D> {
    List<R> getAll();
    R findEntityById(ID id);
    R createEntity(D dto);
    R updateEntityById(ID id, D dto);
    void deleteEntityById(ID id);
}

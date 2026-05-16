package gr.athtech.app.bookmanager.controller;

import gr.athtech.app.bookmanager.service.BaseService;
import gr.athtech.app.bookmanager.transfer.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Base controller for basic CRUD operations
 * @param <R> The response of the entity
 * @param <D> The DTO of the entity
 */
public abstract class BaseController<R, D> {
    protected abstract BaseService<R, Long, D> getBaseService();

    @GetMapping
    public ResponseEntity<ApiResponse<List<R>>> findAll() {
        return ResponseEntity.ok(ApiResponse.<List<R>>builder().data(getBaseService().getAll()).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<R>> getEntityById(@PathVariable final Long id) {
        return ResponseEntity.ok(ApiResponse.<R>builder().data(getBaseService().findEntityById(id)).build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<R>> createEntity(@RequestBody @Valid D dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<R>builder().data(getBaseService().createEntity(dto)).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<R>> updateEntityById(@PathVariable final Long id, @RequestBody @Valid D dto) {
        return ResponseEntity.ok(ApiResponse.<R>builder().data(getBaseService().updateEntityById(id, dto)).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntityById(@PathVariable final Long id) {
        getBaseService().deleteEntityById(id);
        return ResponseEntity.noContent().build();
    }
}

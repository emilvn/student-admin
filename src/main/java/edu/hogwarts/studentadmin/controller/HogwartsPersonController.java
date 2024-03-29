package edu.hogwarts.studentadmin.controller;

import edu.hogwarts.studentadmin.dto.HogwartsPersonDTO;
import edu.hogwarts.studentadmin.model.HogwartsPerson;
import edu.hogwarts.studentadmin.service.HogwartsPersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller superclass for HogwartsPerson entities.
 * @param <M> The type of HogwartsPerson (Student | Teacher).
 * @param <D> The DTO for the HogwartsPerson (StudentDTO | TeacherDTO).
 * @param <S> The service for the HogwartsPerson (StudentService | TeacherService).
 */
public abstract class HogwartsPersonController<M extends HogwartsPerson, D extends HogwartsPersonDTO, S extends HogwartsPersonService<M, D>> {
    protected final S service;

    /**
     * Create a new HogwartsPersonController. Uses dependency injection to set the service.
     * @param service The service for HogwartsPerson entities.
     */
    public HogwartsPersonController(S service) {
        this.service = service;
    }

    /**
     * Handle HTTP GET requests for the / endpoint.
     * Gets all HogwartsPerson entities.
     *
     * @return An HTTP response with a list of HogwartsPerson entities or 204 no content if there are none.
     */
    @GetMapping
    public ResponseEntity<List<D>> getAll() {
        var people = service.getAll();
        if (people.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(people);
    }

    /**
     * Handle HTTP GET requests for the /{id} endpoint.
     * Gets a HogwartsPerson entity by its ID.
     * @param id The ID of the HogwartsPerson entity.
     * @return An HTTP response with the HogwartsPerson entity or 404 not found if it does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<D> get(@PathVariable("id") Long id) {
        var person = service.get(id);
        if (person == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(person);
    }

    /**
     * Handle HTTP POST requests for the / endpoint.
     * Creates a new HogwartsPerson entity from the request body.
     * @param person The HogwartsPerson entity to create.
     * @return An HTTP response with the created HogwartsPerson entity.
     */
    @PostMapping
    public ResponseEntity<D> create(@RequestBody D person) {
        return ResponseEntity.ok(service.create(person));
    }

    /**
     * Handle HTTP PUT requests for the /{id} endpoint.
     * Updates a HogwartsPerson entity by its ID with the request body.
     * @param person The updated HogwartsPerson entity.
     * @param id The ID of the HogwartsPerson entity to update.
     * @return An HTTP response with the updated HogwartsPerson entity or 404 not found if it does not exist.
     */
    @PutMapping("/{id}")
    public ResponseEntity<D> update(@RequestBody D person, @PathVariable("id") Long id) {
        var updatedPerson = service.update(person, id);
        if (updatedPerson == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPerson);
    }

    /**
     * Handle HTTP PATCH requests for the /{id} endpoint.
     * Patch a HogwartsPerson entity by its ID with the request body.
     * Only updates the fields present in the request body.
     * @param person The updated HogwartsPerson entity.
     * @param id The ID of the HogwartsPerson entity to patch.
     * @return An HTTP response with the patched HogwartsPerson entity or 404 not found if it does not exist.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<D> patch(@RequestBody D person, @PathVariable("id") Long id) {
        var updatedPerson = service.patch(person, id);
        if (updatedPerson == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPerson);
    }

    /**
     * Handle HTTP DELETE requests for the /{id} endpoint.
     * Delete a HogwartsPerson entity by its ID.
     * @param id The ID of the HogwartsPerson entity to delete.
     * @return An HTTP response with the deleted HogwartsPerson entity or 404 not found if it does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<D> delete(@PathVariable("id") Long id) {
        var person = service.get(id);
        if (person == null) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.ok(person);
    }
}

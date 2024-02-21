package edu.hogwarts.studentadmin.controller;

import edu.hogwarts.studentadmin.model.HogwartsPerson;
import edu.hogwarts.studentadmin.service.HogwartsPersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for HogwartsPerson entities.
 * @param <M> The type of HogwartsPerson (Student | Teacher).
 * @param <S> The service for the HogwartsPerson (StudentService | TeacherService).
 */
public abstract class HogwartsPersonController<M extends HogwartsPerson, S extends HogwartsPersonService<M>> {
    protected final S service;

    public HogwartsPersonController(S service) {
        this.service = service;
    }

    /**
     * Get all HogwartsPerson entities.
     * @return An HTTP response with a list of HogwartsPerson entities or 204 no content if there are none.
     */
    @GetMapping
    public ResponseEntity<List<M>> getAll() {
        var people = service.getAll();
        if (people.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(people);
    }

    /**
     * Get a HogwartsPerson entity by its ID.
     * @param id The ID of the HogwartsPerson entity.
     * @return An HTTP response with the HogwartsPerson entity or 404 not found if it does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<M> get(@PathVariable("id") Long id) {
        var person = service.get(id);
        if (person == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(person);
    }

    /**
     * Create a new HogwartsPerson entity.
     * @param person The HogwartsPerson entity to create.
     * @return An HTTP response with the created HogwartsPerson entity.
     */
    @PostMapping
    public ResponseEntity<M> create(@RequestBody M person) {
        return ResponseEntity.ok(service.create(person));
    }

    /**
     * Update a HogwartsPerson entity by its ID.
     * @param person The updated HogwartsPerson entity.
     * @param id The ID of the HogwartsPerson entity to update.
     * @return An HTTP response with the updated HogwartsPerson entity or 404 not found if it does not exist.
     */
    @PutMapping("/{id}")
    public ResponseEntity<M> update(@RequestBody M person, @PathVariable("id") Long id) {
        var updatedPerson = service.update(person, id);
        if (updatedPerson == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPerson);
    }

    /**
     * Patch a HogwartsPerson entity by its ID.
     * @param person The updated HogwartsPerson entity.
     * @param id The ID of the HogwartsPerson entity to patch.
     * @return An HTTP response with the patched HogwartsPerson entity or 404 not found if it does not exist.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<M> patch(@RequestBody M person, @PathVariable("id") Long id) {
        var updatedPerson = service.patch(person, id);
        if (updatedPerson == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPerson);
    }

    /**
     * Delete a HogwartsPerson entity by its ID.
     * @param id The ID of the HogwartsPerson entity to delete.
     * @return An HTTP response with the deleted HogwartsPerson entity or 404 not found if it does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<M> delete(@PathVariable("id") Long id) {
        var person = service.get(id);
        if (person == null) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.ok(person);
    }
}

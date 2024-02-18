package edu.hogwarts.studentadmin.controller;

import edu.hogwarts.studentadmin.model.HogwartsPerson;
import edu.hogwarts.studentadmin.service.HogwartsPersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class HogwartsPersonController<M extends HogwartsPerson, S extends HogwartsPersonService<M>> {
    protected final S service;

    public HogwartsPersonController(S service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<M>> getAll() {
        var people = service.getAll();
        if (people.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(people);
    }

    @GetMapping("/{id}")
    public ResponseEntity<M> get(@PathVariable("id") Long id) {
        var person = service.get(id);
        if (person == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(person);
    }

    @PostMapping
    public ResponseEntity<M> create(@RequestBody M person) {
        return ResponseEntity.ok(service.create(person));
    }

    @PutMapping("/{id}")
    public ResponseEntity<M> update(@RequestBody M person, @PathVariable("id") Long id) {
        var updatedPerson = service.update(person, id);
        if (updatedPerson == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPerson);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<M> patch(@RequestBody M person, @PathVariable("id") Long id) {
        var updatedPerson = service.patch(person, id);
        if (updatedPerson == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPerson);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<M> delete(@PathVariable("id") Long id) {
        var person = service.get(id);
        if (person == null) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

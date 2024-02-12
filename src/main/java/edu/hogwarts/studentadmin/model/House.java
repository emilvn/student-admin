package edu.hogwarts.studentadmin.model;

import jakarta.persistence.*;

import java.util.List;

@Entity(name = "house")
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String founder;
    private @ElementCollection
    @CollectionTable(name = "colors", joinColumns = @JoinColumn(name = "house_id"))
    List<String> colors;

    public House() {
    }

    public House(Long id, String name, String founder, List<String> colors) {
        this.id = id;
        this.name = name;
        this.founder = founder;
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFounder() {
        return founder;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }
}

package edu.hogwarts.studentadmin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.List;

/**
 * This class represents a house at Hogwarts.
 */
@Entity(name = "house")
public class House {

    @Id
    private String name;

    private String founder;

    private String primaryColor;
    private String secondaryColor;

    public House() {
    }

    public House(String name, String founder, String primaryColor, String secondaryColor) {
        this.name = name;
        this.founder = founder;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
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
        return List.of(primaryColor, secondaryColor);
    }

    /**
     * Sets the primary and secondary colors of the house.
     * Makes sure that only two colors are set.
     * @param colors List of colors to set.
     */
    public void setColors(List<String> colors) {
        this.primaryColor = colors.get(0);
        this.secondaryColor = colors.get(1);
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", founder='" + founder + '\'' +
                ", colors=" + getColors() +
                "}";
    }
}

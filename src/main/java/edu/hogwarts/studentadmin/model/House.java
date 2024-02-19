package edu.hogwarts.studentadmin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

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

    public void setColors(List<String> colors) {
        this.primaryColor = colors.get(0);
        this.secondaryColor = colors.get(1);
    }

    @JsonIgnore
    public String getPrimaryColor() {
        return primaryColor;
    }

    @JsonIgnore
    public void setPrimaryColor(String color1) {
        this.primaryColor = color1;
    }

    @JsonIgnore
    public String getSecondaryColor() {
        return secondaryColor;
    }

    @JsonIgnore
    public void setSecondaryColor(String color2) {
        this.secondaryColor = color2;
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

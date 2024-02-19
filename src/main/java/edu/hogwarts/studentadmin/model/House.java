package edu.hogwarts.studentadmin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity(name = "house")
public class House {

    @Id
    private String name;

    private String founder;

    private String color1;
    private String color2;

    public House() {
    }

    public House(String name, String founder, String color1, String color2) {
        this.name = name;
        this.founder = founder;
        this.color1 = color1;
        this.color2 = color2;
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
        return List.of(color1, color2);
    }

    public void setColors(List<String> colors) {
        this.color1 = colors.get(0);
        this.color2 = colors.get(1);
    }

    @JsonIgnore
    public String getColor1() {
        return color1;
    }

    @JsonIgnore
    public void setColor1(String color1) {
        this.color1 = color1;
    }

    @JsonIgnore
    public String getColor2() {
        return color2;
    }

    @JsonIgnore
    public void setColor2(String color2) {
        this.color2 = color2;
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

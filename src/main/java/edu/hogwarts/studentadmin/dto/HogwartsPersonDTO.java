package edu.hogwarts.studentadmin.dto;

public abstract class HogwartsPersonDTO {
    private String name;
    private String houseName;

    public HogwartsPersonDTO() {
    }

    public HogwartsPersonDTO(String name, String houseName) {
        this.name = name;
        this.houseName = houseName;
    }

    public String getName() {
        return name;
    }

    public String getHouseName() {
        return houseName;
    }
}

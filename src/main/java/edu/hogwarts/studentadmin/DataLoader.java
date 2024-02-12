package edu.hogwarts.studentadmin;

import edu.hogwarts.studentadmin.model.House;
import edu.hogwarts.studentadmin.repository.HouseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DataLoader implements CommandLineRunner {
    private final HouseRepository houseRepository;

    public DataLoader(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        var gryffindorColors = new ArrayList<String>();
        gryffindorColors.add("Scarlet");
        gryffindorColors.add("Gold");

        var hufflepuffColors = new ArrayList<String>();
        hufflepuffColors.add("Yellow");
        hufflepuffColors.add("Black");

        var ravenclawColors = new ArrayList<String>();
        ravenclawColors.add("Blue");
        ravenclawColors.add("Bronze");

        var slytherinColors = new ArrayList<String>();
        slytherinColors.add("Green");
        slytherinColors.add("Silver");

        var gryffindor = new House(1L, "Gryffindor", "Godric Gryffindor", gryffindorColors);
        var hufflepuff = new House(2L, "Hufflepuff", "Helga Hufflepuff", hufflepuffColors);
        var ravenclaw = new House(3L, "Ravenclaw", "Rowena Ravenclaw", ravenclawColors);
        var slytherin = new House(4L, "Slytherin", "Salazar Slytherin", slytherinColors);


        houseRepository.save(gryffindor);
        houseRepository.save(hufflepuff);
        houseRepository.save(ravenclaw);
        houseRepository.save(slytherin);
    }
}

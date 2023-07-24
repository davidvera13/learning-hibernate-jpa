package com.hibernate.jpa.bootstrap;

import com.hibernate.jpa.domain.joinedtable.ElectricGuitar;
import com.hibernate.jpa.repository.ElectricGuitarRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {
    private final ElectricGuitarRepository electricGuitarRepository;

    public Bootstrap(ElectricGuitarRepository electricGuitarRepository) {
        this.electricGuitarRepository = electricGuitarRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        ElectricGuitar electricGuitar = new ElectricGuitar();
        electricGuitar.setNumberOfPickups(2);
        electricGuitar.setNumberOfStrings(6);
        electricGuitarRepository.save(electricGuitar);

    }
}

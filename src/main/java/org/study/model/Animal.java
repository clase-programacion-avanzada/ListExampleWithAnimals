package org.study.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Animal {

    private UUID id;
    private String name;
    private int age;
    private List<Vaccine> vaccines;

    private final static int EDAD_MINIMA = 0;

    private static final String DEFAULT_NAME = "No nombre";
    public Animal(String id, String name, int age) {
        this.id = UUID.fromString(id);
        this.name = name;
        this.age = age;
        this.vaccines = new ArrayList<>();

    }
    public Animal(String name, int age) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.age = age;
        this.vaccines = new ArrayList<>();

    }

    public Animal() {
        this.id = UUID.randomUUID();
        this.name = DEFAULT_NAME;
        this.age = EDAD_MINIMA;
        this.vaccines = new ArrayList<>();
    }

    public void addVaccine(int volume, String brand) {
        Vaccine vaccine = new Vaccine(volume, brand);
        this.vaccines.add(vaccine);
    }

    public void addVaccine(String id,
                           int volume,
                           String brand,
                           String dateOfApplication) {
        Vaccine vaccine = new Vaccine(id, volume, brand, dateOfApplication);
        this.vaccines.add(vaccine);
    }

    public List<Vaccine> getVaccines() {
        return new ArrayList<>(vaccines);
    }


    public String getName() {

        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public UUID getId() {
        return id;
    }

    public List<String> getUniqueBrands() {
        List<String> uniqueBrands = new ArrayList<>();
        for (Vaccine vaccine : vaccines) {
            if (!uniqueBrands.contains(vaccine.getBrand())) {
                uniqueBrands.add(vaccine.getBrand());
            }
        }
        return uniqueBrands;
    }

    @Override
    public String toString() {
        return
            "id: " + id +
            " nombre: '" + name + '\'' +
            " edad: " + age;
    }
}

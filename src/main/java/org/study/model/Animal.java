package org.study.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Animal {

    // Attributes of the Animal class
    private UUID id;
    private String name;
    private int age;
    private List<Vaccine> vaccines; // A list to store associated vaccines

    // Constants
    private final static int MINIMUM_AGE = 0; // Minimum allowed age
    private static final String DEFAULT_NAME = "No nombre"; // Default name for an animal

    // Constructors

    // Constructor with UUID, name, and age provided (Constructor Overloading)
    // This constructor allows creating an Animal with a specific UUID, name, and age.
    // It's useful when you want to initialize an Animal with known attributes.
    public Animal(String id, String name, int age) {
        this.id = UUID.fromString(id); // Convert the provided string to a UUID
        this.name = name;
        this.age = age;
        this.vaccines = new ArrayList<>(); // Initialize the vaccines list as an empty ArrayList
    }

    // Constructor with random UUID, name, and age provided (Constructor Overloading)
    // This constructor generates a random UUID and allows setting the name and age.
    // It's useful when you want to create an Animal with random attributes.
    public Animal(String name, int age) {
        this.id = UUID.randomUUID(); // Generate a random UUID
        this.name = name;
        this.age = age;
        this.vaccines = new ArrayList<>();
    }

    // Default constructor with random UUID, default name, and minimum age (Constructor Overloading)
    // This constructor sets default values for name and age and generates a random UUID.
    // It's useful when you want to create a generic Animal with default attributes.
    public Animal() {
        this.id = UUID.randomUUID();
        this.name = DEFAULT_NAME;
        this.age = MINIMUM_AGE;
        this.vaccines = new ArrayList<>();
    }


    // Method to add a vaccine with volume and brand
    public void addVaccine(int volume, String brand) {
        Vaccine vaccine = new Vaccine(volume, brand); // Create a new Vaccine object
        this.vaccines.add(vaccine); // Add the vaccine to the list
    }

    // Method to add a vaccine with UUID, volume, brand, and date of application
    public void addVaccine(String id, int volume, String brand, String dateOfApplication) {
        Vaccine vaccine = new Vaccine(id, volume, brand, dateOfApplication); // Create a new Vaccine object
        this.vaccines.add(vaccine); // Add the vaccine to the list
    }

    // Getter method to retrieve a copy of the list of vaccines
    public List<Vaccine> getVaccines() {
        return new ArrayList<>(vaccines); // Return a copy of the vaccines list to prevent external modification
    }

    // Getter method to retrieve the name of the animal
    public String getName() {
        return this.name;
    }

    // Setter method to set the name of the animal
    public void setName(String name) {
        this.name = name;
    }

    // Getter method to retrieve the age of the animal
    public int getAge() {
        return age;
    }

    // Setter method to set the age of the animal
    public void setAge(int age) {
        this.age = age;
    }

    // Getter method to retrieve the UUID of the animal
    public UUID getId() {
        return id;
    }

    // Method to retrieve a list of unique vaccine brands associated with the animal
    public List<String> getUniqueBrands() {
        List<String> uniqueBrands = new ArrayList<>();
        for (Vaccine vaccine : vaccines) {
            // Check if the brand is not already in the list before adding it
            if (!uniqueBrands.contains(vaccine.getBrand())) {
                uniqueBrands.add(vaccine.getBrand());
            }
        }
        return uniqueBrands;
    }

    // Override the toString() method to provide a formatted string representation of the Animal object
    @Override
    public String toString() {
        return "id: " + id + " nombre: '" + name + "' edad: " + age;
    }
}

package org.study.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Animal implements Serializable {

    // Attributes of the Animal class
    private UUID id;
    private String name;
    private int age;
    private List<Vaccine> vaccines; // A list to store associated vaccines

    private Set<UUID> ownerIds;

    // Constants
    private final static int MINIMUM_AGE = 0; // Minimum allowed age
    private static final String DEFAULT_NAME = "No nombre"; // Default name for an animal

    // Constructors

    // Constructor with UUID, name, and age provided (Constructor Overloading)
    // This constructor allows creating an Animal with a specific UUID, name, and age.
    // It's useful when you want to initialize an Animal with known attributes.
    public Animal(String id, String name, int age) {
        validateConstructor(id, name, age); // Validate the provided parameters

        this.id = UUID.fromString(id); // Convert the provided string to a UUID
        this.name = name;
        this.age = age;
        this.vaccines = new ArrayList<>(); // Initialize the vaccines list as an empty ArrayList
        this.ownerIds = new HashSet<>();
    }

    public void addOwnerId(UUID owner) {
        ownerIds.add(owner);
    }

    public Set<UUID> getOwnerIds() {
        return new HashSet<>(ownerIds);
    }

    private void validateConstructor(String id, String name, int age) {

        validateId(id); // Validate the provided UUID
        validateName(name); // Validate the provided name
        validateAge(age); // Validate the provided age

    }

    private void validateConstructor(String name, int age) {

        validateName(name); // Validate the provided name
        validateAge(age); // Validate the provided age

    }

    private void validateId(String id) {
        // Step 1: Check if the provided id is null
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        // Step 2: Check if the provided id is empty
        if (id.isEmpty()) {
            throw new IllegalArgumentException("Id cannot be empty");
        }

    }

    private void validateName(String name) {
        // Step 1: Check if the provided name is null
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }

        // Step 2: Check if the provided name is empty
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

    }

    private void validateAge(int age) {
        // Step 1: Check if the provided age is less than the minimum allowed age
        if (age < MINIMUM_AGE) {
            throw new IllegalArgumentException("Age cannot be less than " + MINIMUM_AGE);
        }

    }

    // Constructor with random UUID, name, and age provided (Constructor Overloading)
    // This constructor generates a random UUID and allows setting the name and age.
    // It's useful when you want to create an Animal with random attributes.
    public Animal(String name, int age) {
        validateConstructor(name, age);

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
        Vaccine vaccine = new Vaccine(volume, brand); // Create a new Vaccine object, if volume and brand are valid, otherwise throw an exception
        this.vaccines.add(vaccine); // Add the vaccine to the list
    }

    public boolean addVaccines(List<Vaccine> vaccines) {
        return this.vaccines.addAll(vaccines);
    }

    // Method to add a vaccine with UUID, volume, brand, and date of application
    public void addVaccine(String id, int volume, String brand, String dateOfApplication) {
        Vaccine vaccine = new Vaccine(id, volume, brand, dateOfApplication); // Create a new Vaccine object, if volume, brand and dates are valid, otherwise throw an exception
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

        validateName(name); // Validate the provided name
        this.name = name;
    }

    // Getter method to retrieve the age of the animal
    public int getAge() {


        return age;
    }

    // Setter method to set the age of the animal
    public void setAge(int age) {

        validateAge(age);
        this.age = age;
    }

    // Getter method to retrieve the UUID of the animal
    public UUID getId() {return id;
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

    public String toCSV(String delimiter) {
        String[] ownerIdsArray = this.ownerIds.stream()
            .map(UUID::toString)
            .toArray(String[]::new);
        String ownerIds = "{" + String.join(",",ownerIdsArray ) + "}";
        return id + delimiter + name + delimiter + age + delimiter + ownerIds ;
    }

}

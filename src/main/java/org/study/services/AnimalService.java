package org.study.services;
import static org.study.services.enums.AnimalCSVHeaders.AGE;
import static org.study.services.enums.AnimalCSVHeaders.ID;
import static org.study.services.enums.AnimalCSVHeaders.NAME;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.study.model.Animal;
import org.study.model.Vaccine;
import org.study.services.enums.AnimalCSVHeaders;
import org.study.services.enums.VaccineCSVHeaders;


public class AnimalService {

    private List<Animal> animalList ;

    public AnimalService() {

        this.animalList = new ArrayList<>();
    }

    /**
     * Adds a new animal to the animal list.
     *
     * @param name The name of the animal.
     * @param age  The age of the animal.
     */
    public void addAnimal(String name, int age) {
        // Create a new Animal object with the given name and age.
        Animal animal = new Animal(name, age);

        // Add the newly created animal to the animal list.
        this.animalList.add(animal);
    }

    /**
     * Adds a vaccine to an animal with the specified name.
     *
     * @param nameOfAnimal The name of the animal to which the vaccine will be added.
     * @param volume       The volume of the vaccine.
     * @param brand        The brand of the vaccine.
     *
     * Note: This method assumes that the name of the animal is unique,
     * and it adds the vaccine to the first animal found with the matching name.
     *
     * The addVaccine method delegates the responsibility of finding the animal by name to the findAnimalByName method.
     * This separation of concerns and delegation of specific tasks is a good practice because it follows
     * the Single Responsibility Principle (SRP) in software design.
     * Each method has a clear and focused responsibility, making the code more maintainable and easier to understand.
     *
     * The chain of responsibilities is important in software development because it promotes modularity,
     * reusability, and easier testing.
     * If you need to change how animals are found or how vaccines are added in the future,
     * you can modify the respective methods without affecting the other parts of the code that use these methods.
     * This separation of concerns reduces code complexity and helps avoid unintended side effects when making changes.
     */
    public void addVaccine(String nameOfAnimal, int volume, String brand) {
        // This is implying that the name of the animal is unique.

        // Step 1: Find the animal by name using the findAnimalByName method.
        Animal animalToAddVaccine = findAnimalByName(nameOfAnimal);

        // Step 2: Add the vaccine to the found animal.
        animalToAddVaccine.addVaccine(volume, brand);
    }


    /**
     * Searches for an animal by its name.
     *
     * @param nameOfAnimal The name of the animal to search for.
     * @return The found animal or null if not found.
     *
     * Note: Returning null can lead to NullPointerExceptions if not handled properly.
     * Consider using Optional<Animal> to improve safety and handle absence gracefully.
     *
     * Reference: https://www.baeldung.com/java-optional
     */
    private Animal findAnimalByName(String nameOfAnimal) {
        for (Animal animal : animalList) {
            if (animal.getName().equals(nameOfAnimal)) {
                return animal;
            }
        }

        // This implementation could be improved using Optional.
        return null;
    }


    /**
     * Searches for an animal by its unique ID.
     *
     * @param id The ID to search for.
     * @return The found animal or null if not found.
     *
     * Note: Returning null can lead to NullPointerExceptions if not handled properly.
     * Consider using Optional<Animal> to improve safety and handle absence gracefully.
     *
     * Reference: https://www.baeldung.com/java-optional
     */
    private Animal findAnimalById(String id) {
        for (Animal animal : animalList) {
            if (animal.getId().toString().equals(id)) {
                return animal;
            }
        }

        // This implementation could be improved using Optional.
        return null;
    }


    /**
     * Retrieves a copy of the list of animals to avoid unintended mutation.
     * Immutable collections offer benefits such as thread safety, simplicity,
     * and prevention of accidental data modification.
     *
     * @return A new ArrayList containing a copy of the animals in the original list.
     */
    public List<Animal> getAnimalList() {
        // We are returning a copy of the list to avoid mutation.

        // Reference: https://web.mit.edu/6.031/www/sp17/classes/09-immutability/

        return new ArrayList<>(this.animalList);
    }



    /**
     * Generates a report of animals along with the number of vaccines each animal has.
     *
     * @return A list of strings describing each animal's name and the number of vaccines it has.
     */
    public List<String> getAnimalReport() {
        List<String> report = new ArrayList<>();

        // Iterate through each animal in the animalList.
        for (Animal animal : animalList) {
            // Create a report string describing the animal's name and the number of vaccines it has.
            String animalReportValue = animal.getName()
                + " Number of vaccines: "
                + animal.getVaccines().size();

            // Add the report string to the list.
            report.add(animalReportValue);
        }

        return report;
    }


    /**
     * Generates a report of unique vaccine brands across all animals.
     *
     * @return A list of unique vaccine brands.
     */
    public List<String> getUniqueBrandsReport() {
        List<String> reportOfUniqueBrands = new ArrayList<>();

        // Iterate through each animal in the animalList.
        for (Animal animal : animalList) {
            List<String> uniqueBrands = animal.getUniqueBrands();

            // Iterate through unique brands for the current animal.
            for (String brand : uniqueBrands) {
                // Check if the brand is not already in the report list to avoid duplicates.
                if (!reportOfUniqueBrands.contains(brand)) {
                    reportOfUniqueBrands.add(brand);
                }
            }
        }

        return reportOfUniqueBrands;
    }

    /**
     * Generates a list of names of all animals in the animalList.
     *
     * @return A list of animal names.
     */
    public List<String> getAnimalNamesInList() {
        List<String> animalsInList = new ArrayList<>();

        // Iterate through each animal in the animalList and add its name to the list.
        for (Animal animal : animalList) {
            animalsInList.add(animal.getName());
        }

        return animalsInList;
    }


    /**
     * Generates a report of animals with expired vaccines.
     *
     * @return A list of strings describing animals with expired vaccines.
     */
    public List<String> getAnimalsPendingOnNextApplicationReport() {
        List<String> reportOfAnimalsPendingOnNextApplication = new ArrayList<>();

        // Iterate through each animal in the animalList.
        for (Animal animal : animalList) {
            // Iterate through the vaccines of the current animal.
            for (Vaccine vaccine : animal.getVaccines()) {
                // Check if the vaccine is expired using the Vaccine.isVaccineExpired method.
                if (Vaccine.isVaccineExpired(vaccine)) {
                    // Create a report string describing the expired vaccine for the current animal.
                    String animalReportValue = animal.getName()
                        + " has "
                        + vaccine.getBrand()
                        + " of "
                        + vaccine.getVolumeInMl()
                        + " ml "
                        + " expired on "
                        + vaccine.getDateOfNextApplication();

                    // Add the report string to the list.
                    reportOfAnimalsPendingOnNextApplication.add(animalReportValue);
                }
            }
        }

        return reportOfAnimalsPendingOnNextApplication;
    }


    /**
     * Loads animal data from a CSV file with the specified format.
     *
     * @param path      The file path to the CSV file containing animal data.
     * @param delimiter The delimiter used in the CSV file to separate values.
     * @return True if animals were loaded successfully, false otherwise.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public boolean loadAnimalsFromCSVFile(String path, String delimiter) throws IOException {
        File file = new File(path);

        // Reference: https://funnelgarden.com/java_read_file/#1b_FilesreadAllLines_Explicit_Encoding
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

        // Store the current size of animalList before adding new animals.
        int sizeBeforeAdding = this.animalList.size();

        // Iterate through each line in the CSV file and parse animal data.
        for (String line : lines) {
            // Split the line into values using the specified delimiter.
            String[] values = line.split(delimiter);

            // Extract animal data from the CSV line.
            String id = values[AnimalCSVHeaders.ID.getIndex()];
            String name = values[AnimalCSVHeaders.NAME.getIndex()];
            int age = Integer.valueOf(values[AnimalCSVHeaders.AGE.getIndex()]);

            // Create an Animal object and add it to the animalList.
            Animal animal = new Animal(id, name, age);
            this.animalList.add(animal);
        }

        // Check if new animals were added by comparing the list size before and after loading.
        return animalList.size() > sizeBeforeAdding;
    }


    /**
     * Loads vaccine data from a CSV file with the specified format.
     *
     * @param path      The file path to the CSV file containing vaccine data.
     * @param delimiter The delimiter used in the CSV file to separate values.
     * @return True if vaccines were loaded successfully, false otherwise.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public boolean loadVaccinesFromCSVFile(String path, String delimiter) throws IOException {
        File file = new File(path);

        // Reference: https://funnelgarden.com/java_read_file/#1b_FilesreadAllLines_Explicit_Encoding
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

        int countOfVaccines = 0;

        // Iterate through each line in the CSV file and parse vaccine data.
        for (String line : lines) {
            // Split the line into values using the specified delimiter.
            String[] values = line.split(delimiter);

            // Extract vaccine data from the CSV line.
            String id = values[VaccineCSVHeaders.ID.getIndex()];
            int volume = Integer.valueOf(values[VaccineCSVHeaders.VOLUME.getIndex()]);
            String brand = values[VaccineCSVHeaders.BRAND.getIndex()];
            String dateOfApplication = values[VaccineCSVHeaders.DATE_OF_APPLICATION.getIndex()];
            String animalId = values[VaccineCSVHeaders.ANIMAL_ID.getIndex()];

            // Find the corresponding animal by ID.
            Animal animal = findAnimalById(animalId);

            // Add the vaccine to the found animal.
            animal.addVaccine(id, volume, brand, dateOfApplication);

            // Increment the count of loaded vaccines.
            countOfVaccines++;
        }

        // Check if any vaccines were loaded.
        return countOfVaccines > 0;
    }

}

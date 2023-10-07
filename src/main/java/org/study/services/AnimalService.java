package org.study.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.study.exceptions.NotFoundException;
import org.study.model.Animal;
import org.study.model.Vaccine;


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
    public void addAnimalToDatabase(String name, int age) {
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
    public void addVaccineToAnimal(String nameOfAnimal, int volume, String brand)
        throws NotFoundException {
        // This is implying that the name of the animal is unique.

        // Step 1: Find the animal by name using the findAnimalByName method.

        Animal animalToAddVaccine = findAnimalByName(nameOfAnimal);

        if (animalToAddVaccine == null) {
            // The animal was not found.
            throw new NotFoundException(String.format("Animal with name %s not found", nameOfAnimal));
        }

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


    private Animal findAnimalByNameUsingStreams(String nameOfAnimal) throws NotFoundException {
        return animalList.stream()
            .filter(animal -> animal.getName().equals(nameOfAnimal))
            .findFirst()
            .orElseThrow(
                () -> new NotFoundException(
                    String.format("Animal with name %s not found", nameOfAnimal)));
    }

    private List<String> findAnimalsByName(String nameOfAnimal) {
        List<String> animalsFound = new ArrayList<>();

        for (Animal animal : animalList) {
            if (animal.getName().equalsIgnoreCase(nameOfAnimal)) {
                animalsFound.add(animal.getName());
            }
        }

        return animalsFound;
    }

    private List<String> findAnimalsByNameUsingStreams(String nameOfAnimal) {
        return animalList.stream()
            .filter(animal -> animal.getName().equalsIgnoreCase(nameOfAnimal))
            .map(Animal::getName)
            .toList();
    }

    /*This method returns a map with the animal ids and their owner ids set:
    * i.e {animalId1: {ownerId1, ownerId2}, animalId2: {ownerId1, ownerId3}}
    * */
    private Map<UUID, Set<UUID>> findAnimalOwnersByName(String nameOfAnimal) {
        return animalList.stream()
            .filter(animal -> animal.getName().equalsIgnoreCase(nameOfAnimal))
            .collect(Collectors.toMap(Animal::getId, Animal::getOwnerIds));
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
    public Animal findAnimalById(UUID id) {
        for (Animal animal : animalList) {
            if (animal.getId().equals(id)) {
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
     * Loads vaccine data from a CSV file with the specified format.
     *
     * @param path      The file path to the CSV file containing vaccine data.
     * @param delimiter The delimiter used in the CSV file to separate values.
     * @param fileService The FileService object used to read the file.
     * @return True if vaccines were loaded successfully, false otherwise.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public boolean loadVaccinesFromCSVFile(String path,
                                           String delimiter,
                                           FileService fileService)
        throws IOException, NotFoundException {

        Map<UUID, List<Vaccine>> vaccinesByAnimalId =
            fileService.loadVaccinesFromCSVFile(path, delimiter);

        // search animal by id and add vaccines to the animal
        for (Map.Entry<UUID, List<Vaccine>> entry : vaccinesByAnimalId.entrySet()) {
            Animal animal = findAnimalById(entry.getKey());

            if (animal == null) {
                throw new NotFoundException(String.format("Error while assigning vaccines to animal: " +
                    "Animal with id %s not found", entry.getKey()));
            }

            boolean vaccineAdded = animal.addVaccines(entry.getValue());

            if (!vaccineAdded) {
                return false;
            }
        }


        return true;
    }


    /**
     * Loads animals data from a CSV file with the specified format.
     *  THIS METHOD DOES NOT OVERWRITE THE ANIMAL LIST. IT ADDS TO THE EXISTING LIST.
     * @param path      The file path to the CSV file containing animals data.
     * @param delimiter The delimiter used in the CSV file to separate values.
     * @param fileService The FileService object used to read the file.
     * @return True if vaccines were loaded successfully, false otherwise.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public boolean loadAnimalsFromCSVFile(String path,
                                          String delimiter,
                                          FileService fileService)
        throws IOException, NotFoundException {

        List<Animal> animals = fileService.loadAnimalsFromCSVFile(path, delimiter);

        return animalList.addAll(animals);
    }



    /**
     * Loads animals from a binary file by deserializing objects.
     *
     * @param filePath The path to the binary file containing serialized animal objects.
     * @param fileService The FileService object used to read the file.
     * @throws IOException            If an I/O error occurs while reading the file.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    public void loadAnimalsFromBinaryFileUsingTheEntireList(String filePath, FileService fileService) throws IOException, ClassNotFoundException {

        List<Animal> animals =
            fileService.loadAnimalsFromBinaryFileUsingTheEntireList(filePath);
        clearAnimalList();
        animalList.addAll(animals);

    }

    private void clearAnimalList() {
        animalList.clear();
    }


    /**
     * Saves the list of animals to a binary file.
     *
     * @param filePath The path to the binary file where the list of animals will be saved.
     * @param fileService The FileService object used to write the file.
     * @throws IOException If an I/O error occurs while writing the file.
     */
    public void saveAnimalsToBinaryFileUsingTheEntireList(String filePath, FileService fileService) throws IOException {

        fileService.saveAnimalsToBinaryFileUsingTheEntireList(filePath, animalList);

    }

    /**
     * Saves the list of animals to a binary file.
     *
     * @param filePath The path to the binary file where the list of animals will be saved.
     * @param fileService The FileService object used to write the file.
     * @throws IOException If an I/O error occurs while writing the file.
     */
    public void saveAnimalsToCSVFile(String filePath, FileService fileService) throws IOException {

        List<String> animalsListToCSV = this.animalList.stream()
            .map(animal -> animal.toCSV(";"))
            .toList();

        fileService.writeTextFile(filePath, animalsListToCSV);

    }

    public UUID addOwnerToAnimal(int animalNumber,
                                 String userName,
                                 OwnerService ownerService)
        throws NotFoundException {


        Animal animal = this.animalList.get(animalNumber);
        UUID ownerId = ownerService.getOwnerByUsername(userName).getId();
        animal.addOwnerId(ownerId);

        return animal.getId();

    }

    public boolean addAnimalToAppointmentQueue(int animalNumber, AttentionQueueService attentionQueueService) {

        Animal animal = this.animalList.get(animalNumber);
        if(attentionQueueService.isAnimalInQueue(animal)) {
            return false;
        }

        attentionQueueService.addAnimalToAttend(animal);
        return true;

    }

    public void writeFileWithAnimalsAndNextVaccineApplication(String path, FileService fileService) throws IOException {

            fileService.writeTextFile(path, getAnimalsPendingOnNextApplicationReport());

    }

    public List<String> getAnimalsAndTheirOwnersReport(OwnerService ownerService) {


        return animalList.stream()
            .map(
                animal -> animal.getName()
                    + " Owners: " + animal.getOwnerIds().stream()
                        .map(ownerId -> ownerService.getOwnerById(ownerId).getName())
                        .collect(Collectors.joining(", ")))
            .toList();
        /*
        List<String> animalsAndTheirOwnersReport = new ArrayList<>();
        // Iterate through each animal in the animalList.
        for (Animal animal : animalList) {
            // Create a report string describing the animal's name and the number of vaccines it has.
            String animalName = animal.getName();
            for (UUID ownerId : animal.getOwnerIds()) {
                String ownerNames +=  ", " ownerService.getOwnerById(ownerId).getName();
            }
            String animalReportValue = animalName
                + " Owners: "
                + ownerNames;

            // Add the report string to the list.
            animalsAndTheirOwnersReport.add(animalReportValue);
        }

        return animalsAndTheirOwnersReport;

         */
    }

    public void addVaccineToAnimalInQueue(AttentionQueueService attentionQueueService, String brand, int volume) {

            Animal animal = attentionQueueService.attendAnimal();
            animal.addVaccine(volume, brand);
    }
}

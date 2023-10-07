package org.study.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.study.exceptions.NotFoundException;
import org.study.model.Animal;
import org.study.model.Owner;
import org.study.model.Vaccine;
import org.study.services.enums.AnimalAttributesEnum;
import org.study.services.enums.OwnerAttributesEnum;
import org.study.services.enums.VaccineAttributesEnum;

public class FileService {


    public static final String COMMA_DELIMITER = ",";
    public static final String OPEN_CURLY_BRACE = "{";
    public static final String CLOSE_CURLY_BRACE = "}";
    public static final String EMPTY_STRING = "";

    /**
     * Saves the list of animals to a binary file.
     *
     * @param filePath The path to the binary file where the list of animals will be saved.
     * @throws IOException If an I/O error occurs while writing the file.
     */
    public void saveAnimalsToBinaryFileUsingTheEntireList(String filePath, List<Animal> animalList) throws IOException {
        // Create a file object representing the binary file to be written.
        File file = new File(filePath);

        //try with resources will close the file automatically
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)){
            // Write the list of animals to the file.
            oos.writeObject(animalList);
        }
    }

    /**
     * Loads animals from a binary file by deserializing the list of animals.
     *
     * @param filePath The path to the binary file containing the list of animals.
     * @throws IOException If an I/O error occurs while reading the file.
     * @throws ClassNotFoundException If the class of the serialized object cannot be found.
     */
    public List<Animal> loadAnimalsFromBinaryFileUsingTheEntireList(String filePath)
        throws IOException, ClassNotFoundException {
        // Create a file object representing the binary file to be read.
        File file = new File(filePath);

        //Here we are not using try with resources
        try(FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            // Read the list of animals from the file.
            return (ArrayList<Animal>) ois.readObject();

        }

    }

    /**
     * Loads animal data from a CSV file with the specified format.
     *
     * @param path      The file path to the CSV file containing animal data.
     * @param delimiter The delimiter used in the CSV file to separate values.
     * @return True if animals were loaded successfully, false otherwise.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public List<Animal> loadAnimalsFromCSVFile(String path, String delimiter) throws IOException {

        File file = new File(path);

        // Reference: https://funnelgarden.com/java_read_file/#1b_FilesreadAllLines_Explicit_Encoding
        List<String> lines =
            Files.readAllLines(file.toPath(),
                StandardCharsets.UTF_8);

        List<Animal> animalList = new ArrayList<>();

        // Iterate through each line in the CSV file and parse animal data.
        for (String line : lines) {
            // Split the line into values using the specified delimiter.
            String[] values = line.split(delimiter);

            // Extract animal data from the CSV line.
            String id =
                values[AnimalAttributesEnum.ID.getIndex()];
            String name =
                values[AnimalAttributesEnum.NAME.getIndex()];
            int age =
                Integer.valueOf(values[AnimalAttributesEnum.AGE.getIndex()]);

            // Create an Animal object and add it to the animalList.
            Animal animal = new Animal(id, name, age);

            // Extract owner IDs from the CSV line.
            // ids in the CSV file: {id1,id2,id3}
            String ownerIds =
                extractElementsBetweenCurlyBraces(values[AnimalAttributesEnum.OWNERS.getIndex()]);

            String [] ownerIdsArray = splitAndDeleteSpaces(ownerIds, COMMA_DELIMITER);

            for(String ownerId : ownerIdsArray){
                animal.addOwnerId(UUID.fromString(ownerId));
            }

            animalList.add(animal);
        }

        // Check if new animals were added by comparing the list size before and after loading.
        return animalList;
    }

    private String extractElementsBetweenCurlyBraces(String setString){

        return setString
            // Remove the curly braces from the string
            .replace(OPEN_CURLY_BRACE, EMPTY_STRING)
            .replace(CLOSE_CURLY_BRACE, EMPTY_STRING);
    }

    private String[] splitAndDeleteSpaces(String stringToSplit, String delimiter){

        return  Arrays.stream(stringToSplit.split(delimiter))
            .map(String::trim)
            .toArray(String[]::new);
    }

    /**
     * Loads vaccine data from a CSV file with the specified format.
     *
     * @param path      The file path to the CSV file containing vaccine data.
     * @param delimiter The delimiter used in the CSV file to separate values.
     * @return A map of animal IDs to vaccines.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public Map<UUID, List<Vaccine>> loadVaccinesFromCSVFile(String path, String delimiter)
        throws IOException, NotFoundException {
        File file = new File(path);

        // Reference: https://funnelgarden.com/java_read_file/#1b_FilesreadAllLines_Explicit_Encoding
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);


        Map<UUID, List<Vaccine>> vaccinesByAnimalId = new HashMap<>();

        // Iterate through each line in the CSV file and parse vaccine data.
        for (String line : lines) {
            // Split the line into values using the specified delimiter.
            String[] values = line.split(delimiter);

            // Extract vaccine data from the CSV line.
            String id = values[VaccineAttributesEnum.ID.getIndex()];
            int volume = Integer.valueOf(values[VaccineAttributesEnum.VOLUME.getIndex()]);
            String brand = values[VaccineAttributesEnum.BRAND.getIndex()];
            String dateOfApplication = values[VaccineAttributesEnum.DATE_OF_APPLICATION.getIndex()];
            String animalId = values[VaccineAttributesEnum.ANIMAL_ID.getIndex()];

            if(!vaccinesByAnimalId.containsKey(animalId)){
                vaccinesByAnimalId.put(UUID.fromString(animalId), new ArrayList<>());
            }

            // Create a Vaccine object and add it to the map of vaccines.
            Vaccine vaccine = new Vaccine(id, volume, brand, dateOfApplication);

            vaccinesByAnimalId.get(UUID.fromString(animalId)).add(vaccine);



        }

        return vaccinesByAnimalId;
    }

    /**
     * Writes a text file.
     *
     * @param path      The file path to the CSV file containing animal data.
     * @param linesToWrite The lines to write to the file
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public void writeTextFile(String path,
                                     List<String> linesToWrite)
        throws IOException {

        //https://www.baeldung.com/java-write-to-file
        File file = new File(path);

        Files.write(file.toPath(), linesToWrite, StandardCharsets.UTF_8);
    }

    /**
     * Loads Owner data from a CSV file with the specified format.
     *
     * @param path      The file path to the CSV file containing animal data.
     * @param delimiter The delimiter used in the CSV file to separate values.
     * @return True if animals were loaded successfully, false otherwise.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public List<Owner> loadOwnersFromCSVFile(String path, String delimiter) throws IOException {

        File file = new File(path);

        // Reference: https://funnelgarden.com/java_read_file/#1b_FilesreadAllLines_Explicit_Encoding
        List<String> lines =
            Files.readAllLines(file.toPath(),
                StandardCharsets.UTF_8);

        List<Owner> ownersList = new ArrayList<>();

        // Iterate through each line in the CSV file and parse animal data.
        for (String line : lines) {
            // Split the line into values using the specified delimiter.
            String[] values = line.split(delimiter);

            // Extract Owner data from the CSV line.
            String id =
                values[OwnerAttributesEnum.ID.getIndex()];
            String name =
                values[OwnerAttributesEnum.NAME.getIndex()];
            String username =
                values[OwnerAttributesEnum.USERNAME.getIndex()];
            String email =
                values[OwnerAttributesEnum.EMAIL.getIndex()];
            String password =
                values[OwnerAttributesEnum.PASSWORD.getIndex()];
            int age =
                Integer.valueOf(values[OwnerAttributesEnum.AGE.getIndex()]);
            String phone =
                values[OwnerAttributesEnum.PHONE.getIndex()];
            String address =
                values[OwnerAttributesEnum.ADDRESS.getIndex()];
            String city =
                values[OwnerAttributesEnum.CITY.getIndex()];
            String state =
                values[OwnerAttributesEnum.STATE.getIndex()];
            String country =
                values[OwnerAttributesEnum.COUNTRY.getIndex()];
            String zipcode =
                values[OwnerAttributesEnum.ZIPCODE.getIndex()];

            // Create an Owner object and add it to the animalList.
            Owner owner =
                new Owner(id,
                    name,
                    username,
                    email,
                    password,
                    age,
                    phone,
                    address,
                    city,
                    state,
                    country,
                    zipcode);

            // Extract animal IDs from the CSV line.
            // animal ids in the CSV file: {id1,id2,id3}
            String animalIds =
                extractElementsBetweenCurlyBraces(values[OwnerAttributesEnum.ANIMAL_IDS.getIndex()]);

            String [] animalIdsArray = splitAndDeleteSpaces(animalIds, COMMA_DELIMITER);

            for(String ownerId : animalIdsArray){
                owner.addAnimalId(UUID.fromString(ownerId));
            }

            ownersList.add(owner);
        }

        // Check if new animals were added by comparing the list size before and after loading.
        return ownersList;
    }


    public List<Owner> loadOwnersFromBinaryFileUsingTheEntireList(String filePath)
        throws IOException, ClassNotFoundException {

        File file = new File(filePath);

        //Here we are not using try with resources
        try(FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            // Read the list of animals from the file.
            return (ArrayList<Owner>) ois.readObject();

        }

    }

    public void saveOwnersToBinaryFileUsingTheEntireList(String filePath, List<Owner> owners)
        throws IOException {

        File file = new File(filePath);

        //try with resources will close the file automatically
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)){
            // Write the list of animals to the file.
            oos.writeObject(owners);
        }

    }





}

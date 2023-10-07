package org.study.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.study.exceptions.NotFoundException;
import org.study.model.Owner;
import org.study.exceptions.UserNameAlreadyTakenException;

public class OwnerService {

    Map<UUID, Owner> ownersById;
    Map<String, Owner> ownersByUsername;

    public OwnerService() {
        this.ownersById = new HashMap<>();
        this.ownersByUsername = new HashMap<>();
    }


    public boolean addOwnerToDatabase(
        String name,
        String username,
        String email,
        String password,
        int age,
        String phone,
        String address,
        String city,
        String state,
        String zip,
        String country

    ) throws UserNameAlreadyTakenException, IllegalArgumentException {
        if (usernameIsTaken(username)) {
            throw new UserNameAlreadyTakenException(String.format("Username %s is already taken", username));
        }

        Owner owner = new Owner(name, username, email, password, age, phone, address, city, state, zip, country);
        //put method returns null if the key is not present in the map
        return addOwnerToDatabase(owner);

    }

    private boolean addOwnerToDatabase(Owner owner) throws IllegalArgumentException {
        if (owner == null) {
            throw new IllegalArgumentException("Owner cannot be null");
        }
        return ownersById.put(owner.getId(), owner) == null && ownersByUsername.put(owner.getUsername(), owner) == null;
    }

    private boolean usernameIsTaken(String username) {
        return ownersById.values().stream()
            .noneMatch(owner -> owner.getUsername().equalsIgnoreCase(username));
    }

    public Owner getOwnerById(UUID ownerId) throws NullPointerException {
        return ownersById.get(ownerId);
    }

    public Owner getOwnerByUsername(String username) throws NullPointerException, NotFoundException {
        if(!ownersByUsername.containsKey(username)){
            throw new NotFoundException(String.format("Owner with username %s not found", username));
        }
        return ownersByUsername.get(username);
    }

    public boolean deleteOwnerById(UUID ownerId) throws NullPointerException, NotFoundException {

        if (!ownersById.containsKey(ownerId)) {
            throw new NotFoundException(String.format("Owner with id %s not found", ownerId));
        }

        Owner owner = ownersById.get(ownerId);
        return ownersById.remove(ownerId) != null && ownersByUsername.remove(owner.getUsername()) != null;
    }

    public void addAnimalIdToOwner(String username, UUID animalId) throws NotFoundException {
        Owner owner = getOwnerByUsername(username);
        owner.addAnimalId(animalId);

    }

    public void removeOwnerFromOwnerByUsername(String username, UUID ownerId) throws NotFoundException {
        Owner owner = getOwnerByUsername(username);
        owner.removeAnimalId(ownerId);
    }

    protected void clearDatabase() {
        ownersById.clear();
        ownersByUsername.clear();
    }

    public Map<UUID, Owner> getOwnersById() {
        return new HashMap<>(ownersById);
    }

    public Map<String, Owner> getOwnersByUsername() {
        return new HashMap<>(ownersByUsername);
    }

    /**
     * Loads owners data from a CSV file with the specified format.
     *  THIS METHOD DOES NOT OVERWRITE THE OWNERS LIST. IT ADDS TO THE EXISTING Database.
     * @param path      The file path to the CSV file containing owners data.
     * @param delimiter The delimiter used in the CSV file to separate values.
     * @param fileService The FileService object used to read the file.
     * @return True if ALL owners were added to the database, false if at least one of them already existed.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public boolean loadOwnersFromCSVFile(String path,
                                          String delimiter,
                                          FileService fileService)
        throws IOException, NotFoundException {

        List<Owner> owners = fileService.loadOwnersFromCSVFile(path, delimiter);

        return addOwnersToDatabase(owners);
    }

    private boolean addOwnersToDatabase(List<Owner> owners) {

        return owners.stream().allMatch(
            owner -> addOwnerToDatabase(owner)
        );
    }


    /**
     * Loads owners from a binary file by deserializing objects.
     *
     * @param filePath The path to the binary file containing serialized owner objects.
     * @param fileService The FileService object used to read the file.
     * @throws IOException            If an I/O error occurs while reading the file.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    public void loadOwnersFromBinaryFileUsingTheEntireList(String filePath,
                                                           FileService fileService) throws IOException, ClassNotFoundException {

        List<Owner> owners =
            fileService.loadOwnersFromBinaryFileUsingTheEntireList(filePath);
        clearDatabase();

        addOwnersToDatabase(owners);

    }


    /**
     * Saves the list of owners to a binary file.
     *
     * @param filePath The path to the binary file where the list of owners will be saved.
     * @param fileService The FileService object used to write the file.
     * @throws IOException If an I/O error occurs while writing the file.
     */
    public void saveOwnersToBinaryFileUsingTheEntireList(String filePath,
                                                         FileService fileService) throws IOException {

        List<Owner> owners = new ArrayList<>(ownersById.values());
        fileService.saveOwnersToBinaryFileUsingTheEntireList(filePath, owners);

    }

    /**
     * Saves the list of owners to a binary file.
     *
     * @param filePath The path to the binary file where the list of owners will be saved.
     * @param fileService The FileService object used to write the file.
     * @throws IOException If an I/O error occurs while writing the file.
     */
    public void saveOwnersToCSVFile(String filePath, FileService fileService) throws IOException {


        List<String> ownersListToCSV = this.ownersById.values().stream()
            .map(owner -> owner.toCSV(";"))
            .toList();

        fileService.writeTextFile(filePath, ownersListToCSV);

    }


    public List<String> getOwnersAndTheirAnimalsReport(AnimalService animalService) {
        return ownersById.values().stream()
            .map(owner -> owner.getName()
                        + owner.getUsername() + " owns :"
                        + owner.getAnimalIds().stream()
                            .map(animalId -> animalService.findAnimalById(animalId).getName())
                            .collect(Collectors.joining(", "))
            )
            .toList();

    }
}

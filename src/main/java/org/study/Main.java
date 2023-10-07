package org.study;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import org.study.exceptions.ExitMethodException;
import org.study.exceptions.NotFoundException;
import org.study.exceptions.UserNameAlreadyTakenException;
import org.study.services.AnimalService;
import org.study.services.AttentionQueueService;
import org.study.services.FileService;
import org.study.services.OwnerService;

public class Main {

    // Define constants for default values
    private static final String DEFAULT_DELIMITER = ";";
    private static final String YES = "y";
    private static final String ANIMALS_CSV_DEFAULT_PATH = "src/main/resources/animals.csv";

    private static final String ANIMALS_CSV_REPORT_DEFAULT_PATH = "src/main/resources/animals_report.csv";
    private static final String ANIMALS_BIN_DEFAULT_PATH = "src/main/resources/animals.bin";

    private static final String OWNERS_BIN_DEFAULT_PATH = "src/main/resources/owners.bin";

    private static final String OWNERS_CSV_DEFAULT_PATH = "src/main/resources/owners.csv";
    private static final String VACCINES_CSV_DEFAULT_PATH = "src/main/resources/vaccines.csv";
    public static final String DO_YOU_WANT_TO_SAVE_TO_BINARY_FILE_TEMPLATE =
        "Do you want to save %s to binary file? (y/n)";
    public static final String PLEASE_ENTER_THE_PATH_OF_THE_CSV_FILE = "Please enter the path of the CSV file";

    public static void main(String[] args) {

        // Initialize a scanner for user input and an AnimalService instance
        Scanner scanner = new Scanner(System.in);
        AnimalService animalService = new AnimalService();
        OwnerService ownerService = new OwnerService();
        FileService fileService = new FileService();
        AttentionQueueService attentionQueueService = new AttentionQueueService();

        // Display a welcome message and present a menu to the user
        System.out.println("Welcome to the animal app");
        int option = -1;
        do {

            printMenu();

            // Read the user's choice
            try {
                option = scanner.nextInt();
                //Reference: https://docs.oracle.com/javase/7/docs/api/java/util/Scanner.html#nextLine()
                //Reference: https://www.freecodecamp.org/news/java-scanner-nextline-call-gets-skipped-solved/
                scanner.nextLine();



                // Perform actions based on the user's choice using a switch statement
                //Reference: https://docs.oracle.com/javase/tutorial/java/nutsandbolts/switch.html
                //Reference: https://medium.com/@javatechie/the-evolution-of-switch-statement-from-java-7-to-java-17-4b5eee8d29b7
                switch(option) {

                    case 0 -> loadProgramStateFromBinaryFiles(scanner, animalService, ownerService, fileService);

                    case 1 -> addAnimalToDatabase(scanner, animalService);
                    case 2 -> addOwnerToDatabase(scanner, ownerService);
                    case 3 -> addOwnerToExistingAnimal(scanner, animalService, ownerService);
                    case 4 -> addVaccineToNextAnimalInQueue(scanner, animalService, attentionQueueService);
                    case 5 -> addAnimalToAppointmentQueue(scanner, animalService, attentionQueueService);

                    case 6 -> printReportOfOwnersAndTheirAnimals(ownerService, animalService);
                    case 7 -> printReportOfAnimalsAndTheirOwners(animalService, ownerService);
                    case 8 -> printReportOfAnimalsPendingOnNextApplication(animalService);
                    case 9 -> printReportOfAnimalVaccines(animalService);
                    case 10 -> printReportOfUniqueBrands(animalService);

                    case 11 -> writeFileWithAnimalsAndNextVaccineApplication(scanner, animalService, fileService);
                    case 12 -> loadAnimalsAndVaccinesFromCSV(scanner, animalService, fileService);
                    case 13 -> loadOwnersFromCSV(scanner, ownerService, fileService);
                    case 14 -> saveStateToBinaryFile(scanner, animalService, ownerService, fileService);
                    case 15 -> System.out.println("Exiting the program");

                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid option, please enter a number");
                scanner.nextLine(); // Consume the newline character
            } catch (ExitMethodException e) {
                System.out.println("Going back to main menu");
            }
        } while(option != 15); // Continue looping until the user selects option 8 (Exit)

        // Close the scanner when done
        scanner.close();
    }

    private static void printMenu() {
        //Reference: https://docs.oracle.com/en/java/javase/17/text-blocks/index.html
        System.out.println("""
                    0. Load program state from binary file
                    
                    1. Add animal to database
                    2. Add Owner to database
                    3. Add Owner to existing animal
                    4. Add vaccine animal in queue
                    5. Add animal to appointment queue
       
                    6. Print report of owners and their animals
                    7. Print report of animals and their owners
                    8. Print report of animals pending on next vaccine application
                    9. print report of animal vaccines
                    10. print report of unique brands
                    
                    11. write file with animals and next vaccine application
                    12. load animals and vaccines from CSV
                    13. load owners from CSV
                    14. save program state to binary file
                    
                    15. Exit
                    
                    Please enter your option
                    """);
    }


    //===========CASE 0 - LOAD PROGRAM STATE FROM BINARY FILE===========
    private static void loadProgramStateFromBinaryFiles(Scanner scanner,
                                                        AnimalService animalService,
                                                        OwnerService ownerService,
                                                        FileService fileService) throws ExitMethodException {

        System.out.println("""
              Do you want to load state from binary file? (y/n)
              THIS OPERATION WILL OVERWRITE THE CURRENT LIST OF ANIMALS AND OWNERS 
              """);
        String loadState = scanner.nextLine();

        if(!loadState.equalsIgnoreCase(YES)) {
            throw new ExitMethodException();
        }

        loadAnimalsFromBinaryFile(scanner, animalService, fileService);

        loadOwnersFromBinaryFile(scanner, ownerService, fileService);
    }

    private static void loadAnimalsFromBinaryFile(Scanner scanner,
                                                  AnimalService animalService,
                                                  FileService fileService) {
        System.out.println("Please enter the path of the animals binary file");
        String path = getPath(scanner, ANIMALS_BIN_DEFAULT_PATH);

        try{
            // Attempt to load animals from the specified binary file
            animalService.loadAnimalsFromBinaryFileUsingTheEntireList(path,fileService);

            System.out.println("Animals loaded successfully");

        } /*We can catch multiple exceptions using | */
        catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading animals due to error: " + e.getMessage());
        }
    }

    private static void loadOwnersFromBinaryFile(Scanner scanner,
                                                 OwnerService ownerService,
                                                 FileService fileService) {

        System.out.println("Please enter the path of the owners binary file");
        String path = getPath(scanner, OWNERS_BIN_DEFAULT_PATH);

        try{
            // Attempt to load owners from the specified binary file
            ownerService.loadOwnersFromBinaryFileUsingTheEntireList(path, fileService);

            System.out.println("Owners loaded successfully");

        } /*We can catch multiple exceptions using | */
        catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading owners due to error: " + e.getMessage());
        }
    }

    //CASE 1 - ADD ANIMAL TO DATABASE
    private static void addAnimalToDatabase(Scanner scanner, AnimalService animalService) {
        try {
            System.out.println("Please enter the name of the animal");
            String name = scanner.nextLine();
            System.out.println("Please enter the age of the animal");
            int age = Integer.valueOf(scanner.nextLine());
            animalService.addAnimalToDatabase(name, age);
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating animal due to :" + e.getMessage());
        }
    }

    //===========CASE 2 - ADD OWNER TO DATABASE===========
    private  static void addOwnerToDatabase(Scanner scanner, OwnerService ownerService) {
        try {
            System.out.println("Please enter the name of the owner");
            String name = scanner.nextLine();
            System.out.println("Please enter the username of the owner");
            String username = scanner.nextLine();
            System.out.println("Please enter the email of the owner, remember to follow the format");
            String email = scanner.nextLine();
            System.out.println("""
                Please enter the address of the owner, remember to follow the format
                    -Password cannot be empty
                    -Password must contain at least 8 characters
                    -Password must contain at least one digit
                    -Password must contain at least one lower case character
                    -Password must contain at least one upper case character
                    -Password must contain at least one special character from # ? ! @ $   % ^ & * -
                """);
            String password = scanner.nextLine();
            System.out.println("Please enter the owners age, remember that minimum age is 18");
            int age = Integer.valueOf(scanner.nextLine());

            System.out.println("Please enter the phone of the owner");
            String phone = scanner.nextLine();

            System.out.println("Please enter the address of the owner");
            String address = scanner.nextLine();

            System.out.println("Please enter the city of the owner");
            String city = scanner.nextLine();

            System.out.println("Please enter the state of the owner");
            String state = scanner.nextLine();

            System.out.println("Please enter the zip of the owner");
            String zip = scanner.nextLine();

            System.out.println("Please enter the country of the owner");
            String country = scanner.nextLine();

            ownerService.addOwnerToDatabase(name,
                username,
                email,
                password,
                age,
                phone,
                address,
                city,
                state,
                zip,
                country);



        } catch (IllegalArgumentException | UserNameAlreadyTakenException e) {
            System.out.println("Error creating owner due to :" + e.getMessage());
        }
    }


    //===========CASE 3 - ADD OWNER TO EXISTING ANIMAL===========
    private static void addOwnerToExistingAnimal(Scanner scanner, AnimalService animalService, OwnerService ownerService) {

        printAnimalsForSelection(animalService);

        System.out.println("Enter the number of the animal you want to add the owner to");
        try {
            int animalNumber = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter the username of the owner");
            String username = scanner.nextLine();

            UUID animalId = animalService.addOwnerToAnimal(animalNumber, username, ownerService);
            ownerService.addAnimalIdToOwner(username, animalId);

        } catch (NumberFormatException | IndexOutOfBoundsException | NotFoundException e) {
            System.out.println("Error adding owner to animal due to error: " + e.getMessage());
        }

    }

    //===========CASE 4 - ADD VACCINE TO NEXT ANIMAL IN QUEUE============
    // Helper method to add a vaccine to an animal
    private static void addVaccineToNextAnimalInQueue(Scanner scanner,
                                                      AnimalService animalService,
                                                      AttentionQueueService attentionQueueService)
        throws ExitMethodException {

        if (!attentionQueueService.isThereAnyAnimalToAttend()) {
            System.out.println("There are no animals to attend");
            throw new ExitMethodException();
        }

        System.out.println("Do you want to add a vaccine to the next animal in the queue? (y/n)");
        String addVaccine = scanner.nextLine();

        if(!addVaccine.equalsIgnoreCase(YES)) {
            throw new ExitMethodException();
        }

        System.out.println("Please enter the brand of the vaccine");
        String brand = scanner.nextLine();

        System.out.println("Please enter the volume in ml");
        int volume = Integer.parseInt(scanner.nextLine());

        animalService.addVaccineToAnimalInQueue(attentionQueueService, brand, volume);

        System.out.println("Vaccine added successfully");

    }

    //===========CASE 5 - ADD ANIMAL TO APPOINTMENT QUEUE===========
    private static void addAnimalToAppointmentQueue(Scanner scanner,
                                                    AnimalService animalService,
                                                    AttentionQueueService attentionQueueService) {

        printAnimalsForSelection(animalService);

        System.out.println("Enter the number of the animal you want to add to the appointment queue");
        try {
            int animalNumber = Integer.parseInt(scanner.nextLine());

            boolean isAnimalInQueue =  animalService.addAnimalToAppointmentQueue(animalNumber, attentionQueueService);

            if (isAnimalInQueue) {
                System.out.println("Animal added to appointment queue successfully");
            } else {
                System.out.println("Animal already in appointment queue");
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("Error adding animal to appointment queue due to error: " + e.getMessage());
        }

    }

    //=========CASE 6 - PRINT REPORT OF OWNERS AND THEIR ANIMALS==========
    private static void printReportOfOwnersAndTheirAnimals(OwnerService ownerService,
                                                           AnimalService animalService) {

        List<String> reportOfOwnersAndTheirAnimals =
            ownerService.getOwnersAndTheirAnimalsReport(animalService);
        System.out.println("The report of owners and their animals is:");
        printReport(reportOfOwnersAndTheirAnimals);



    }

    //===========CASE 7 - PRINT REPORT OF ANIMALS AND THEIR OWNERS=========
    private static void printReportOfAnimalsAndTheirOwners(AnimalService animalService,
                                                           OwnerService ownerService) {

        List<String> reportOfAnimalsAndTheirOwners =
            animalService.getAnimalsAndTheirOwnersReport(ownerService);
        System.out.println("The report of animals and their owners is:");
        printReport(reportOfAnimalsAndTheirOwners);

    }

    //===============CASE 8 - PRINT REPORT OF ANIMALS PENDING ON NEXT APPLICATION======
    // Helper method to print a report of animals pending on the next application
    private static void printReportOfAnimalsPendingOnNextApplication(AnimalService animalService) {
        List<String> reportOfAnimalsPendingOnNextApplication =
            animalService.getAnimalsPendingOnNextApplicationReport();
        System.out.println("The animals pending on the next application are:");
        printReport(reportOfAnimalsPendingOnNextApplication);
    }

    //==============CASE 9 - PRINT REPORT OF ANIMAL VACCINES=============
    // Helper method to print a report of animal vaccines
    private static void printReportOfAnimalVaccines(AnimalService animalService) {
        List<String> reportOfAnimalVaccines =
            animalService.getAnimalReport();
        System.out.println("The report of animal vaccines is:");
        printReport(reportOfAnimalVaccines);
    }

    //=================CASE 10 - PRINT REPORT OF UNIQUE BRANDS==============
    // Helper method to print a report of unique brands
    private static void printReportOfUniqueBrands(AnimalService animalService) {
        List<String> reportOfUniqueBrands = animalService.getUniqueBrandsReport();
        System.out.println("The unique brands are:");
        printReport(reportOfUniqueBrands);
    }
    private static void printAnimalsForSelection(AnimalService animalService) {

        List<String> animalNames = animalService.getAnimalNamesInList();
        System.out.println("The current animals are:");
        for(int i = 0; i < animalNames.size(); i++) {
            System.out.println(i + ". " + animalNames.get(i));
        }
    }

    //===============CASE 11 - WRITE FILE WITH ANIMALS AND NEXT VACCINE APPLICATION=========
    private static void writeFileWithAnimalsAndNextVaccineApplication(Scanner scanner, AnimalService animalService, FileService fileService)
        throws ExitMethodException {
        System.out.println("Do you want to write a CSV file with animals and next vaccine application? (y/n)");
        String writeCSVFile = scanner.nextLine();

        if(!writeCSVFile.equalsIgnoreCase(YES)) {
            throw new ExitMethodException();
        }

        System.out.println(PLEASE_ENTER_THE_PATH_OF_THE_CSV_FILE);
        String path = getPath(scanner, ANIMALS_CSV_REPORT_DEFAULT_PATH);

        try{
            // Attempt to save animals to the specified file
            //If something is wrong it will throw an exception
            animalService.writeFileWithAnimalsAndNextVaccineApplication(path, fileService);

            System.out.println("File with animals and next vaccine application saved successfully");

        } catch (IOException e) {
            System.out.println(
                "Error saving file with animals and next vaccine application due to error: "
                    + e.getMessage());
        }

    }

    //============CASE 12 - LOAD ANIMALS AND VACCINES FROM CSV===========
    // Helper method to load animals and vaccines from CSV
    private static void loadAnimalsAndVaccinesFromCSV(Scanner scanner,
                                                      AnimalService animalService,
                                                      FileService fileService) throws ExitMethodException {

        loadAnimalsFromCSV(scanner, animalService, fileService);

        // Ask the user if they want to load vaccines from CSV
        System.out.println("Do you want to load vaccines from CSV? (y/n)");
        String loadVaccines = scanner.nextLine();

        if( loadVaccines.equalsIgnoreCase(YES)) {
            loadVaccinesFromCSV(scanner, animalService, fileService);
        }
    }

    private static void loadAnimalsFromCSV(Scanner scanner, AnimalService animalService, FileService fileService)
        throws ExitMethodException {

        // Ask the user for the CSV file path and delimiter
        String path = getPath(scanner, ANIMALS_CSV_DEFAULT_PATH);
        String delimiter = getDelimiter(scanner);

        //Reference: https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
        //Reference: https://www.geeksforgeeks.org/try-catch-throw-and-throws-in-java/
        try{
            // Attempt to load animals from the specified CSV file
            boolean loaded = animalService.loadAnimalsFromCSVFile(path, delimiter, fileService);
            if (loaded) {
                System.out.println("Animals loaded successfully");
            } else {
                System.out.println("Animals not loaded");
            }
        } catch (IOException | NotFoundException | IllegalArgumentException e) {
            System.out.println("Error loading animals due to error: " + e.getMessage());
            throw new ExitMethodException();
        }

    }

    // Helper method to load vaccines from CSV
    private static void loadVaccinesFromCSV(Scanner scanner,
                                            AnimalService animalService,
                                            FileService fileService) throws ExitMethodException {
        String path = getPath(scanner, VACCINES_CSV_DEFAULT_PATH);
        String delimiter = getDelimiter(scanner);

        try{
            // Attempt to load vaccines from the specified CSV file
            boolean loaded = animalService.loadVaccinesFromCSVFile(path, delimiter, fileService );
            if (loaded) {
                System.out.println("Vaccines loaded successfully");
            } else {
                System.out.println("Vaccines not loaded");
            }
        } catch (IOException | NotFoundException | IllegalArgumentException e) {
            System.out.println("Error loading vaccines due to error: " + e.getMessage());
            throw new ExitMethodException();
        }
    }

    //=========CASE 13 - LOAD OWNERS FROM CSV============
    private static void loadOwnersFromCSV(Scanner scanner, OwnerService ownerService, FileService fileService)
        throws ExitMethodException {

        System.out.println("Do you want to load owners from CSV? (y/n)");
        String loadOwners = scanner.nextLine();

        if(!loadOwners.equalsIgnoreCase(YES)) {
            throw new ExitMethodException();
        }

        System.out.println(PLEASE_ENTER_THE_PATH_OF_THE_CSV_FILE);
        String path = getPath(scanner, OWNERS_CSV_DEFAULT_PATH);

        System.out.println("Please enter the delimiter");
        String delimiter = getDelimiter(scanner);

        try{
            // Attempt to load owners from the specified CSV file
            boolean loaded = ownerService.loadOwnersFromCSVFile(path, delimiter, fileService);
            if (loaded) {
                System.out.println("Owners loaded successfully");
            } else {
                System.out.println("Owners not loaded");
            }
        } catch (IOException | NotFoundException | IllegalArgumentException e) {
            System.out.println("Error loading owners due to error: " + e.getMessage());
        }

    }

    //========CASE 14 - SAVE PROGRAM STATE TO BINARY FILE==========
    private static void saveStateToBinaryFile(Scanner scanner,
                                              AnimalService animalService,
                                              OwnerService ownerService,
                                              FileService fileService) throws ExitMethodException {
        System.out.println("""
              Do you want to save state from binary file? (y/n)
              THIS OPERATION WILL OVERWRITE THE CURRENT LIST OF ANIMALS AND OWNERS THAT IS SAVED 
              """);
        String saveState = scanner.nextLine();

        if(!saveState.equalsIgnoreCase(YES)) {
            throw new ExitMethodException();
        }

        saveOwnersToBinaryFile(scanner, ownerService, fileService);
        saveAnimalsToBinaryFile(scanner, animalService, fileService);

    }

    private static void saveOwnersToBinaryFile(Scanner scanner,
                                               OwnerService ownerService,
                                               FileService fileService) throws ExitMethodException {

        System.out.println(
            String.format(DO_YOU_WANT_TO_SAVE_TO_BINARY_FILE_TEMPLATE, "owners"));
        String saveOwners = scanner.nextLine();

        if(!saveOwners.equalsIgnoreCase(YES)) {
            throw new ExitMethodException();
        }

        System.out.println("Please enter the path of the binary file");
        String path = getPath(scanner, OWNERS_BIN_DEFAULT_PATH);

        try{
            // Attempt to save owners to the specified binary file
            //If something is wrong it will throw an exception
            ownerService.saveOwnersToBinaryFileUsingTheEntireList(path, fileService);

            System.out.println("Owners saved successfully");


        } catch (IOException e) {
            System.out.println("Error saving owners due to error: " + e.getMessage());
        }
    }

    private static void saveAnimalsToBinaryFile(Scanner scanner,
                                                AnimalService animalService,
                                                FileService fileService) throws ExitMethodException {

        System.out.println(
            String.format(DO_YOU_WANT_TO_SAVE_TO_BINARY_FILE_TEMPLATE, "animals"));
        String saveAnimals = scanner.nextLine();

        if(!saveAnimals.equalsIgnoreCase(YES)) {
            throw new ExitMethodException();
        }

        System.out.println("Please enter the path of the binary file");
        String path = getPath(scanner, ANIMALS_BIN_DEFAULT_PATH);

        try{
            // Attempt to save animals to the specified binary file
            //If something is wrong it will throw an exception
            animalService.saveAnimalsToBinaryFileUsingTheEntireList(path, fileService);

            System.out.println("Animals saved successfully");


        } catch (IOException e) {
            System.out.println("Error saving animals due to error: " + e.getMessage());
        }


    }

    //OTHER METHODS
    // Helper method to get the file path files
    private static String getPath(Scanner scanner, String defaultPath) {
        System.out.println("Load using default path? (y/n) " + defaultPath + " is the default");
        String useDefaultPath = scanner.nextLine();

        if(!useDefaultPath.equalsIgnoreCase(YES)) {
            System.out.println("Please enter the path of the file");
        }

        //Reference: https://www.geeksforgeeks.org/java-ternary-operator-with-examples/
        return useDefaultPath.equalsIgnoreCase(YES)
            ? defaultPath
            : scanner.nextLine();
    }

    // Helper method to get the delimiter for CSV files
    private static String getDelimiter(Scanner scanner) {
        System.out.println("Load using default delimiter? (y/n) " + DEFAULT_DELIMITER + " is the default");
        String useDefaultDelimiter = scanner.nextLine();

        if (!useDefaultDelimiter.equalsIgnoreCase(YES)) {
            System.out.println("Please enter the delimiter");
        }

        return useDefaultDelimiter.equalsIgnoreCase(YES)
            ? DEFAULT_DELIMITER
            : scanner.nextLine();
    }













    // Helper method to print the names of current animals
    private static void printAnimalNames(AnimalService animalService) {
        System.out.println("The current animals are:");
        for(String animalName : animalService.getAnimalNamesInList()) {
            System.out.println(animalName);
        }
    }



    // Helper method to print a report
    private static void printReport(List<String> report) {
        for(String reportValue : report) {
            System.out.println(reportValue);
        }
    }
}

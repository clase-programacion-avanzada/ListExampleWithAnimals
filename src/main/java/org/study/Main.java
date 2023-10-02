package org.study;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import org.study.services.AnimalService;

public class Main {

    // Define constants for default values
    private static final String DEFAULT_DELIMITER = ";";
    private static final String YES = "y";
    private static final String ANIMALS_CSV_DEFAULT_PATH = "src/main/resources/animals.csv";
    private static final String ANIMALS_BIN_DEFAULT_PATH = "src/main/resources/animals.bin";

    private static final String VACCINES_CSV_DEFAULT_PATH = "src/main/resources/vaccines.csv";
    public static void main(String[] args) {

        // Initialize a scanner for user input and an AnimalService instance
        Scanner scanner = new Scanner(System.in);
        AnimalService animalService = new AnimalService();

        // Display a welcome message and present a menu to the user
        System.out.println("Welcome to the animal app");
        int option = 0;
        do {
            //Reference: https://docs.oracle.com/en/java/javase/17/text-blocks/index.html
            System.out.println("""
                    0. Load animals from binary file
                    1. Add animal
                    2. Add vaccine to animal
                    3. Load animals and vaccines from CSV
                    4. Print report of unique brands
                    5. Print report of animal vaccines
                    6. Print report of animals pending on nextApplication
                    7. Save animals to binary file
                    8. Exit
                    Please enter your option
                    """);

            // Read the user's choice
            option = scanner.nextInt();
            //Reference: https://docs.oracle.com/javase/7/docs/api/java/util/Scanner.html#nextLine()
            //Reference: https://www.freecodecamp.org/news/java-scanner-nextline-call-gets-skipped-solved/
            scanner.nextLine();

            // Perform actions based on the user's choice using a switch statement
            //Reference: https://docs.oracle.com/javase/tutorial/java/nutsandbolts/switch.html
            //Reference: https://medium.com/@javatechie/the-evolution-of-switch-statement-from-java-7-to-java-17-4b5eee8d29b7
            switch(option) {
                case 0 -> loadAnimalsFromBinaryFile(scanner, animalService); // Option 0: Load animals from binary file
                case 1 -> createNewAnimal(scanner, animalService); // Option 1: Add a new animal
                case 2 -> addVaccineToAnimal(scanner, animalService); // Option 2: Add a vaccine to an animal
                case 3 -> loadAnimalsAndVaccinesFromCSV(scanner, animalService); // Option 3: Load animals and vaccines from CSV
                case 4 -> printReportOfUniqueBrands(animalService); // Option 4: Print a report of unique brands
                case 5 -> printReportOfAnimalVaccines(animalService); // Option 5: Print a report of animal vaccines
                case 6 -> printReportOfAnimalsPendingOnNextApplication(animalService); // Option 6: Print a report of animals pending on next application
                case 7 -> saveAnimalsToBinaryFile(scanner, animalService); // Option 7: Save animals to binary file
                case 8 -> System.out.println("Bye"); // Option 8: Exit the program
                default -> System.out.println("Invalid option"); // Invalid option
            }
        } while(option != 8); // Continue looping until the user selects option 8 (Exit)

        // Close the scanner when done
        scanner.close();
    }

    private static void saveAnimalsToBinaryFile(Scanner scanner, AnimalService animalService) {

        System.out.println("Do you want to save animals to binary file? (y/n)");
        String saveAnimals = scanner.nextLine();

        if(! saveAnimals.equalsIgnoreCase(YES)) {
            System.out.println("Going back to main menu");
        }

        System.out.println("Please enter the path of the binary file");
        String path = getPath(scanner, ANIMALS_BIN_DEFAULT_PATH);

        try{
            // Attempt to save animals to the specified binary file
            animalService.saveAnimalsToBinaryFileUsingTheEntireList(path);

            System.out.println("Animals saved successfully");

        } catch (IOException e) {
            System.out.println("Error saving animals due to error: " + e.getMessage());
        }


    }

    private static void loadAnimalsFromBinaryFile(Scanner scanner, AnimalService animalService) {

        System.out.println("Do you want to load animals from binary file? (y/n)");
        String loadAnimals = scanner.nextLine();

        if(! loadAnimals.equalsIgnoreCase(YES)) {
            System.out.println("Going back to main menu");
        }

        System.out.println("Please enter the path of the binary file");
        String path = getPath(scanner, ANIMALS_BIN_DEFAULT_PATH);

        try{
            // Attempt to load animals from the specified binary file
            animalService.loadAnimalsFromBinaryFileUsingTheEntireList(path);

            System.out.println("Animals loaded successfully");

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading animals due to error: " + e.getMessage());
        }
    }

    // Helper method to load animals and vaccines from CSV
    private static void loadAnimalsAndVaccinesFromCSV(Scanner scanner, AnimalService animalService)  {
        // Ask the user for the CSV file path and delimiter
        String path = getPath(scanner, ANIMALS_CSV_DEFAULT_PATH);
        String delimiter = getDelimiter(scanner);

        //Reference: https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
        //Reference: https://www.geeksforgeeks.org/try-catch-throw-and-throws-in-java/
        try{
            // Attempt to load animals from the specified CSV file
            boolean loaded = animalService.loadAnimalsFromCSVFile(path, delimiter);
            if (loaded) {
                System.out.println("Animals loaded successfully");
            } else {
                System.out.println("Animals not loaded");
            }
        } catch (IOException e) {
            System.out.println("Error loading animals");
        }

        // Ask the user if they want to load vaccines from CSV
        System.out.println("Do you want to load vaccines from CSV? (y/n)");
        String loadVaccines = scanner.nextLine();

        if( loadVaccines.equalsIgnoreCase(YES)) {
            loadVaccines(scanner, animalService);
        }
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

    // Helper method to get the file path for CSV files
    private static String getPath(Scanner scanner, String defaultPath) {
        System.out.println("Load using default path? (y/n)" + defaultPath + " is the default");
        String useDefaultPath = scanner.nextLine();

        if(!useDefaultPath.equalsIgnoreCase(YES)) {
            System.out.println("Please enter the path of the CSV file");
        }

        return useDefaultPath.equalsIgnoreCase(YES)
            ? defaultPath
            : scanner.nextLine();
    }

    // Helper method to load vaccines from CSV
    private static void loadVaccines(Scanner scanner, AnimalService animalService) {
        String path = getPath(scanner, VACCINES_CSV_DEFAULT_PATH);
        String delimiter = getDelimiter(scanner);

        try{
            // Attempt to load vaccines from the specified CSV file
            boolean loaded = animalService.loadVaccinesFromCSVFile(path, delimiter);
            if (loaded) {
                System.out.println("Vaccines loaded successfully");
            } else {
                System.out.println("Vaccines not loaded");
            }
        } catch (IOException e) {
            System.out.println("Error loading vaccines");
        }
    }

    // Helper method to print a report of animals pending on the next application
    private static void printReportOfAnimalsPendingOnNextApplication(AnimalService animalService) {
        List<String> reportOfAnimalsPendingOnNextApplication =
            animalService.getAnimalsPendingOnNextApplicationReport();
        System.out.println("The animals pending on the next application are:");
        printReport(reportOfAnimalsPendingOnNextApplication);
    }

    // Helper method to print a report of animal vaccines
    private static void printReportOfAnimalVaccines(AnimalService animalService) {
        List<String> reportOfAnimalVaccines =
            animalService.getAnimalReport();
        System.out.println("The report of animal vaccines is:");
        printReport(reportOfAnimalVaccines);
    }

    // Helper method to print a report of unique brands
    private static void printReportOfUniqueBrands(AnimalService animalService) {
        List<String> reportOfUniqueBrands = animalService.getUniqueBrandsReport();
        System.out.println("The unique brands are:");
        printReport(reportOfUniqueBrands);
    }

    // Helper method to add a vaccine to an animal
    private static void addVaccineToAnimal(Scanner scanner, AnimalService animalService) {
        // Show a list of current animals using animalService method getAnimalNamesInList
        printAnimalNames(animalService);
        System.out.println("Please enter the name of the animal to add the vaccine");
        String nameOfAnimal = scanner.nextLine();
        System.out.println("Please enter the volume of the vaccine");
        int volume = Integer.parseInt(scanner.nextLine());
        System.out.println("Please enter the brand of the vaccine");
        String brand = scanner.nextLine();
        animalService.addVaccine(nameOfAnimal, volume, brand);
    }

    // Helper method to print the names of current animals
    private static void printAnimalNames(AnimalService animalService) {
        System.out.println("The current animals are:");
        for(String animalName : animalService.getAnimalNamesInList()) {
            System.out.println(animalName);
        }
    }

    // Helper method to create a new animal
    private static void createNewAnimal(Scanner scanner, AnimalService animalService) {
        System.out.println("Please enter the name of the animal");
        String name = scanner.nextLine();
        System.out.println("Please enter the age of the animal");
        int age = Integer.valueOf(scanner.nextLine());
        animalService.addAnimal(name, age);
    }

    // Helper method to print a report
    private static void printReport(List<String> report) {
        for(String reportValue : report) {
            System.out.println(reportValue);
        }
    }
}

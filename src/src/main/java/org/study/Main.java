package org.study;


import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import org.study.services.AnimalService;

public class Main {

    private static final String DEFAULT_DELIMITER = ";";
    private static final String YES = "y";

    private static final String ANIMALS_CSV_DEFAULT_PATH = "src/main/resources/animals.csv";
    private static final String VACCINES_CSV_DEFAULT_PATH = "src/main/resources/vaccines.csv";

    public static void main(String[] args) {

        //Main with a menu to create animals using animalService to add animals
        //and to add vaccines to animals

        Scanner scanner = new Scanner(System.in);
        AnimalService animalService = new AnimalService();

        System.out.println("Welcome to the animal app");
        int option = 0;
        do {
            System.out.println("""
                    1. Add animal
                    2. Add vaccine to animal
                    3. Load animals and vaccines from CSV
                    4. Print report of unique brands
                    5. Print report of animal vaccines
                    6. Print report of animals pending on nextApplication
                    8. Exit""");

            option = scanner.nextInt();
            scanner.nextLine();

            switch(option) {
                case 1 -> createNewAnimal(scanner, animalService);
                case 2 -> addVaccineToAnimal(scanner, animalService);
                case 3 -> loadAnimalsAndVaccinesFromCSV(scanner, animalService);
                case 4 -> printReportOfUniqueBrands(animalService);
                case 5 -> printReportOfAnimalVaccines(animalService);
                case 6 -> printReportOfAnimalsPendingOnNextApplication(animalService);
                case 8 -> System.out.println("Bye");
                default -> System.out.println("Invalid option");

            }
        } while(option != 8);

        scanner.close();

    }

    private static void loadAnimalsAndVaccinesFromCSV(Scanner scanner, AnimalService animalService)  {

        String path = getPath(scanner, ANIMALS_CSV_DEFAULT_PATH);

        String delimiter = getDelimiter(scanner);

        try{
            boolean loaded = animalService.loadAnimalsFromCSVFile(path, delimiter);
            if (loaded) {
                System.out.println("Animals loaded successfully");
            } else {
                System.out.println("Animals not loaded");
            }

        } catch (IOException e) {
            System.out.println("Error loading animals");
        }

        System.out.println("Do you want to load vaccines from CSV? (y/n)");
        String loadVaccines = scanner.nextLine();

        if( loadVaccines.equalsIgnoreCase(YES)) {
            loadVaccines(scanner, animalService);
        }


    }

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

    private static void loadVaccines(Scanner scanner, AnimalService animalService) {


        String path = getPath(scanner, VACCINES_CSV_DEFAULT_PATH);
        String delimiter = getDelimiter(scanner);

        try{
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

    private static void printReportOfAnimalsPendingOnNextApplication(AnimalService animalService) {
        List<String> reportOfAnimalsPendingOnNextApplication =
            animalService.getAnimalsPendingOnNextApplicationReport();
        System.out.println("The animals pending on next application are:");
        printReport(reportOfAnimalsPendingOnNextApplication);
    }

    private static void printReportOfAnimalVaccines(AnimalService animalService) {
        List<String> reportOfAnimalVaccines =
            animalService.getAnimalReport();
        System.out.println("The report of animal vaccines is:");
        printReport(reportOfAnimalVaccines);
    }

    private static void printReportOfUniqueBrands(AnimalService animalService) {
        List<String> reportOfUniqueBrands = animalService.getUniqueBrandsReport();
        System.out.println("The unique brands are:");
        printReport(reportOfUniqueBrands);
    }

    private static void addVaccineToAnimal(Scanner scanner, AnimalService animalService) {
        //Show list of current animals using animalService method getAnimalNamesInList

        printAnimalNames(animalService);
        System.out.println("Please enter the name of the animal to add the vaccine");
        String nameOfAnimal = scanner.nextLine();
        System.out.println("Please enter the volume of the vaccine");
        int volume = Integer.valueOf(scanner.nextLine());
        System.out.println("Please enter the brand of the vaccine");
        String brand = scanner.nextLine();
        animalService.addVaccine(nameOfAnimal, volume, brand);
    }

    private static void printAnimalNames(AnimalService animalService) {
        System.out.println("The current animals are:");
        for(String animalName : animalService.getAnimalNamesInList()) {
            System.out.println(animalName);
        }

    }

    private static void createNewAnimal(Scanner scanner, AnimalService animalService) {
        System.out.println("Please enter the name of the animal");
        String name = scanner.nextLine();
        System.out.println("Please enter the age of the animal");
        int age = Integer.valueOf(scanner.nextLine());
        animalService.addAnimal(name, age);


    }

    private static void printReport(List<String> report) {

        for(String reportValue : report) {
            System.out.println(reportValue);
        }
    }


}


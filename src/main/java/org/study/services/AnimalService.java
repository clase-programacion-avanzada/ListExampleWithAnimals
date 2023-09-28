package org.study.services;
import static org.study.services.enums.AnimalCSVHeaders.AGE;
import static org.study.services.enums.AnimalCSVHeaders.ID;
import static org.study.services.enums.AnimalCSVHeaders.NAME;

import java.io.File;
import java.io.IOException;
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

    public void addAnimal(String name, int age) {
        Animal animal = new Animal(name, age);

        this.animalList.add(animal);
    }

    public void addVaccine(String nameOfAnimal,
                           int volume,
                           String brand) {

        //This is implying that name of animal is unique
        Animal animalToAddVaccine =
            findAnimalByName(nameOfAnimal);

        animalToAddVaccine.addVaccine(volume,brand);
    }

    private Animal findAnimalByName(String nameOfAnimal) {

        for (Animal animal : animalList) {
            if (animal.getName().equals(nameOfAnimal)) {
                return animal;
            }
        }

        //this implementation could be improved using Optional
        //https://www.baeldung.com/java-optional
        return null;

    }

    private Animal findAnimalById(String id) {

        for (Animal animal : animalList) {
            if (animal.getId().toString().equals(id)) {
                return animal;
            }
        }

        //this implementation could be improved using Optional
        //https://www.baeldung.com/java-optional
        return null;

    }

    public List<Animal> getAnimalList() {

        //We are returning a copy of the list to avoid mutation
        //https://web.mit.edu/6.031/www/sp17/classes/09-immutability/
        return new ArrayList<>(this.animalList);
    }

    public List<String> getAnimalReport() {
        List<String> report =  new ArrayList<>();

        for(Animal animal : animalList ) {
            String animalReportValue =
                animal.getName()
                    + " Number of vaccines: "
                    + animal.getVaccines().size();

            report.add(animalReportValue);
        }

        return report;
    }

    public List<String> getUniqueBrandsReport() {

        List<String> reportOfUniqueBrands =  new ArrayList<>();


        for(Animal animal : animalList ) {
            List<String> uniqueBrands =
                animal.getUniqueBrands();
            for(String brand : uniqueBrands) {
                if(!reportOfUniqueBrands.contains(brand)) {
                    reportOfUniqueBrands.add(brand);
                }
            }

        }

        return reportOfUniqueBrands;
    }

    public List<String> getAnimalNamesInList() {

        List<String> animalsInList = new ArrayList<>();

        for(Animal animal : animalList) {
            animalsInList.add(animal.getName());
        }

        return animalsInList;

    }

    public List<String> getAnimalsPendingOnNextApplicationReport() {

        List<String> reportOfAnimalsPendingOnNextApplication = new ArrayList<>();

        for(Animal animal : animalList) {
            for (Vaccine vaccine : animal.getVaccines()) {

                if(Vaccine.isVaccineExpired(vaccine)) {
                    String animalReportValue =
                        animal.getName()
                            + " has "
                            + vaccine.getBrand()
                            + " of "
                            + vaccine.getVolumeInMl()
                            + " ml "
                            + " expired on "
                            + vaccine.getDateOfNextApplication();
                    reportOfAnimalsPendingOnNextApplication.add(animalReportValue);
                }
            }
        }

        return reportOfAnimalsPendingOnNextApplication;
    }

    /*Loads file from path with following format
    id;name; age
    The file doesn't include vaccines information*/
    public boolean loadAnimalsFromCSVFile(String path,
                                          String delimiter) throws IOException {

        File file = new File(path);

        List <String> lines = Files.readAllLines(file.toPath());

        int sizeBeforeAdding = this.animalList.size();

        for(String line : lines) {
            String[] values = line.split(delimiter);
            String id = values[AnimalCSVHeaders.ID.getIndex()];
            String name = values[AnimalCSVHeaders.NAME.getIndex()];
            int age = Integer.valueOf(values[AnimalCSVHeaders.AGE.getIndex()]);
            Animal animal = new Animal(id, name, age);
            this.animalList.add(animal);
        }

        return animalList.size() > sizeBeforeAdding;

    }

    public boolean loadVaccinesFromCSVFile(String path,
                                           String delimiter) throws IOException {

        File file = new File(path);

        List <String> lines = Files.readAllLines(file.toPath());


        int countOfVaccines = 0;

        for(String line : lines) {
            String[] values = line.split(delimiter);

            String id = values[VaccineCSVHeaders.ID.getIndex()];
            int volume = Integer.valueOf(values[VaccineCSVHeaders.VOLUME.getIndex()]);
            String brand = values[VaccineCSVHeaders.BRAND.getIndex()];
            String dateOfApplication =
                values[VaccineCSVHeaders.DATE_OF_APPLICATION.getIndex()];
            String animalId = values[VaccineCSVHeaders.ANIMAL_ID.getIndex()];
            Animal animal = findAnimalById(animalId);
            animal.addVaccine(id, volume, brand, dateOfApplication);
            countOfVaccines++;
        }

        return countOfVaccines > 0;



    }
}

package org.study.model;

import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Vaccine implements Serializable {


    private UUID id;
    private int volumeInMl;
    private String brand;

    // Reference: https://www.baeldung.com/java-date-to-localdate-and-localdatetime
    // Reference: https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html
    private LocalDate dateOfApplication;

    // Reference: https://www.geeksforgeeks.org/static-keyword-java/
    // Reference: https://www.geeksforgeeks.org/final-keyword-java/
    // Reference: https://www.baeldung.com/java-static
    // Reference: https://www.baeldung.com/java-final
    private static final long SIX_MONTHS = 6;

    // Reference: https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
    // Reference: https://www.baeldung.com/java-datetimeformatter
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    // Constructor 1: Takes four parameters (overloaded)
    public Vaccine(String id, int volumeInMl, String brand, String dateOfApplication) {
        validateConstructor(id, volumeInMl, brand, dateOfApplication);


        // Step 1: Parse the given 'id' string into a UUID using UUID.fromString().
        // This method creates a UUID from a string representation.
        this.id = UUID.fromString(id);

        // Step 2: Set the 'volumeInMl' and 'brand' attributes based on the provided parameters.
        this.volumeInMl = volumeInMl;
        this.brand = brand;

        // Step 3: Parse the 'dateOfApplication' string into a LocalDate using LocalDate.parse().
        // The date format is defined in DATE_FORMAT, and it's used to parse the string into a date object.
        // The parsed date is assigned to the 'dateOfApplication' attribute.
        // Reference: https://www.geeksforgeeks.org/localdate-parse-method-in-java-with-examples/
        this.dateOfApplication = LocalDate.parse(dateOfApplication, DATE_FORMAT);
    }



    // Constructor 2: Takes two parameters (overloaded)
    public Vaccine(int volumeInMl, String brand) {

        validateConstructor(volumeInMl, brand);

        // Step 1: Set the 'volumeInMl' and 'brand' attributes based on the provided parameters.
        this.id = UUID.randomUUID();
        this.volumeInMl = volumeInMl;
        this.brand = brand;

        // Step 2: Set the 'dateOfApplication' to the current date using LocalDate.now().
        // This constructor assumes that the date of application is the current date.
        this.dateOfApplication = LocalDate.now();
    }



    // Constructor 3: Takes three parameters (overloaded) and may throw a ParseException
    public Vaccine(int volumeInMl, String brand, String dateOfApplication) throws ParseException {
        validateConstructor(volumeInMl, brand, dateOfApplication);

        // Step 1: Set the 'volumeInMl' and 'brand' attributes based on the provided parameters.
        this.id = UUID.randomUUID();
        this.volumeInMl = volumeInMl;
        this.brand = brand;

        // Step 2: Parse the 'dateOfApplication' string into a LocalDate using LocalDate.parse().
        // The date format is defined in DATE_FORMAT, and it's used to parse the string into a date object.
        // The parsed date is assigned to the 'dateOfApplication' attribute.
        // Reference: https://www.geeksforgeeks.org/localdate-parse-method-in-java-with-examples/
        // Note: This constructor can throw a ParseException if the date string is not in the expected format.
        this.dateOfApplication = LocalDate.parse(dateOfApplication, DATE_FORMAT);
    }

    private void validateConstructor(String id, int volumeInMl, String brand, String dateOfApplication) {

        validateId(id);
        validateVolumeInMl(volumeInMl);
        validateBrand(brand);
        validateDateOfApplication(dateOfApplication);

    }

    private void validateConstructor(int volumeInMl, String brand) {
        validateVolumeInMl(volumeInMl);
        validateBrand(brand);
    }

    private void validateConstructor(int volumeInMl, String brand, String dateOfApplication) {
        validateVolumeInMl(volumeInMl);
        validateBrand(brand);
        validateDateOfApplication(dateOfApplication);
    }

    private void validateDateOfApplication(String dateOfApplication) {

        //throws IllegalArgumentException if dateOfApplication is null, empty or not in the expected format
        if(dateOfApplication == null) {
            throw new IllegalArgumentException("Date of application cannot be null");

        }

        if (dateOfApplication.isEmpty()) {
            throw new IllegalArgumentException("Date of application cannot be empty");
        }

        if (!dateOfApplication.matches("\\d{2}/\\d{2}/\\d{4}")) {
            throw new IllegalArgumentException("Date of application must be in the format dd/MM/yyyy");
        }

    }

    private void validateBrand(String brand) {
        if (brand == null ) {
            throw new IllegalArgumentException("Brand cannot be null");
        }
        if(brand.isEmpty()) {
            throw new IllegalArgumentException("Brand cannot be empty");
        }
    }

    private void validateId(String id) {

        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
    }

    private void validateVolumeInMl(int volumeInMl) {

        if( volumeInMl < 0) {
            throw new IllegalArgumentException("Volume in ml cannot be negative or zero");
        }

    }



    public int getVolumeInMl() {
        return volumeInMl;
    }

    public void setVolumeInMl(int volumeInMl) {

        validateVolumeInMl(volumeInMl);
        this.volumeInMl = volumeInMl;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {

        validateBrand(brand);
        this.brand = brand;
    }

    public LocalDate getDateOfApplication() {
        return dateOfApplication;
    }

    public void setDateOfApplication(LocalDate dateOfApplication) {
        this.dateOfApplication = dateOfApplication;
    }

    public UUID getID() {
        return id;
    }

    public static boolean isVaccineExpired(Vaccine vaccine) {
        // Step 1: Get the current date using the LocalDate.now() method.
        // LocalDate represents a date (year, month, day) without a time zone.
        //https://www.geeksforgeeks.org/localdate-now-method-in-java-with-examples/
        LocalDate currentDate = LocalDate.now();

        // Step 2: Check if the current date is after the date of the next application.
        // The isAfter() method of LocalDate is used to compare two LocalDate objects.
        // It returns true if the current date is chronologically after the dateOfNextApplication.
        // In this context, if the current date is later than the dateOfNextApplication,
        // it means the vaccine has expired.
        //https://www.geeksforgeeks.org/localdate-isafter-method-in-java-with-examples/
        return currentDate.isAfter(vaccine.getDateOfNextApplication());
    }
    public ChronoLocalDate getDateOfNextApplication() {
        // Step 1: Calculate the date of the next application.
        // The date of the next application is obtained by adding six months to the
        // date of the current application (dateOfApplication).
        // The plusMonths() method is used to add a specified number of months to a date.
        // In this case, SIX_MONTHS (a constant representing six months) is added.
        return this.dateOfApplication.plusMonths(SIX_MONTHS);
    }

    @Override
    public String toString() {
        return "Vaccine{" +
            " volume in ml =" + volumeInMl +
            ", brand ='" + brand + '\'' +
            ", date of application =" + dateOfApplication +
            ", date of next application =" + getDateOfNextApplication() +
            '}';
    }
}

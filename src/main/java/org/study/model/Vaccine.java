package org.study.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class Vaccine {

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
        // Step 1: Set the 'volumeInMl' and 'brand' attributes based on the provided parameters.
        this.volumeInMl = volumeInMl;
        this.brand = brand;

        // Step 2: Set the 'dateOfApplication' to the current date using LocalDate.now().
        // This constructor assumes that the date of application is the current date.
        this.dateOfApplication = LocalDate.now();
    }

    // Constructor 3: Takes three parameters (overloaded) and may throw a ParseException
    public Vaccine(int volumeInMl, String brand, String dateOfApplication) throws ParseException {
        // Step 1: Set the 'volumeInMl' and 'brand' attributes based on the provided parameters.
        this.volumeInMl = volumeInMl;
        this.brand = brand;

        // Step 2: Parse the 'dateOfApplication' string into a LocalDate using LocalDate.parse().
        // The date format is defined in DATE_FORMAT, and it's used to parse the string into a date object.
        // The parsed date is assigned to the 'dateOfApplication' attribute.
        // Reference: https://www.geeksforgeeks.org/localdate-parse-method-in-java-with-examples/
        // Note: This constructor can throw a ParseException if the date string is not in the expected format.
        this.dateOfApplication = LocalDate.parse(dateOfApplication, DATE_FORMAT);
    }


    public int getVolumeInMl() {
        return volumeInMl;
    }

    public void setVolumeInMl(int volumeInMl) {
        this.volumeInMl = volumeInMl;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
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

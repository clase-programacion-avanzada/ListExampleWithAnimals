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
    private LocalDate dateOfApplication;
    private static final long SIX_MONTHS = 6;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Vaccine(String id, int volumeInMl, String brand, String dateOfApplication) {

        this.id = UUID.fromString(id);
        this.volumeInMl = volumeInMl;
        this.brand = brand;
        this.dateOfApplication = LocalDate.parse(dateOfApplication, DATE_FORMAT);


    }
    public Vaccine(int volumeInMl, String brand) {

        this.volumeInMl = volumeInMl;
        this.brand = brand;
        this.dateOfApplication = LocalDate.now();


    }

    public Vaccine(int volumeInMl, String brand, String dateOfApplication)
        throws ParseException {

        this.volumeInMl = volumeInMl;
        this.brand = brand;
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


    public static boolean isVaccineExpired(Vaccine vaccine) {
        LocalDate currentDate = LocalDate.now();
        return currentDate.isAfter(vaccine.getDateOfNextApplication());
    }

    public ChronoLocalDate getDateOfNextApplication() {
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

package org.study.services.enums;

public enum VaccineCSVHeaders {

    ID(0,"id"),
    VOLUME(1,"volume"),
    BRAND(2,"brand"),
    DATE_OF_APPLICATION(3,"dateOfApplication"),
    ANIMAL_ID(4,"animalId");

    private int index;
    private String headerName;

    VaccineCSVHeaders(int index, String headerName) {
        this.index = index;
        this.headerName = headerName;
    }

    public int getIndex() {
        return index;
    }

    public String getHeaderName() {
        return headerName;
    }


}

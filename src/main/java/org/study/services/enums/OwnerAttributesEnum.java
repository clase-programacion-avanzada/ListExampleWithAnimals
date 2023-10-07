package org.study.services.enums;

public enum OwnerAttributesEnum {

    ID(0,"id"),
    NAME(1,"name"),
    USERNAME(2,"username"),
    EMAIL(3,"email"),
    PASSWORD(4,"password"),
    AGE(5,"age"),
    PHONE(6,"phone"),
    ADDRESS(7,"address"),
    CITY(8,"city"),
    STATE(9,"state"),
    COUNTRY(10,"country"),

    ZIPCODE(11, "zipcode"),
    ANIMAL_IDS(12, "animalIds");

    private int index;

    private String header;

    private OwnerAttributesEnum(int index, String header) {
        this.index = index;
        this.header = header;

    }

    public String getHeader() {
        return header;
    }

    public int getIndex() {
        return index;
    }
}

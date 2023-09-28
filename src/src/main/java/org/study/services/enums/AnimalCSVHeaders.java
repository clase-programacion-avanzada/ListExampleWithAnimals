package org.study.services.enums;

public enum AnimalCSVHeaders {

    ID(0,"id"),
    NAME(1,"name"),
    AGE(2,"age");


    private int index;
    private String headerName;

    AnimalCSVHeaders(int index, String headerName) {
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

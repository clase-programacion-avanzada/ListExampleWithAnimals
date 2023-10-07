package org.study.services.enums;

/**
 * Enum representing CSV headers for animal data.
 *
 * References:
 * - Reference: https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html
 * - Reference: https://www.baeldung.com/a-guide-to-java-enums
 * - Reference: https://www.geeksforgeeks.org/enum-in-java/
 */

/*Explanation of how enums work in Java:

Enums in Java are a special type of data type that represents a fixed set of constants or values.
They are similar to classes in structure but are used to define a collection of related constants.

In this AnimalCSVHeaders enum, we define three constants (ID, NAME, and AGE) that represent headers for animal data in
a CSV file.

Each constant can have fields and methods just like a regular class.
 In this case, we have two fields: index and headerName, which store the index of the header in a CSV file
 and the header name, respectively.

Enums are often used when you have a fixed set of related constants that you want to use throughout your code.
They provide type-safety, making it clear what values are valid for a particular variable or parameter.

You can access the enum constants using the enum name (e.g., AnimalCSVHeaders.ID)
and access their fields and methods accordingly.

Enums are particularly useful for scenarios where you want to represent a collection of closely related values,
such as options, states, or headers, as demonstrated in this AnimalCSVHeaders enum.*/
public enum AnimalAttributesEnum {

    ID(0, "id"),
    NAME(1, "name"),
    AGE(2, "age"),
    OWNERS(3, "owners");

    private int index;
    private String headerName;

    /**
     * Constructor for AnimalCSVHeaders enum.
     *
     * @param index       The index of the header in a CSV file.
     * @param headerName  The name of the header.
     */
    AnimalAttributesEnum(int index, String headerName) {
        this.index = index;
        this.headerName = headerName;
    }

    /**
     * Get the index of the header.
     *
     * @return The index of the header.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Get the name of the header.
     *
     * @return The name of the header.
     */
    public String getHeaderName() {
        return headerName;
    }
}

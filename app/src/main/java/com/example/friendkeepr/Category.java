//******************************************************************************
//  Category.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This class represents a group of 'Friend' objects, with a unique group name
//  and number of days until a reminder is given to contact them.
//******************************************************************************
package com.example.friendkeepr;

public class Category {

    public static final String CSV_DELIMITER = "|";
    public static final String CSV_DELIMITER_REGEX = "\\|";

    private final String name;
    private final int daysToReminder;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public Category(String name, int daysToReminder) {
        this.name = name;
        this.daysToReminder = daysToReminder;
    }

    //--------------------------------------------------------------------------
    //  This static constructor creates the instance from a string that would be
    //  one row of a csv file.
    //--------------------------------------------------------------------------
    public static Category fromCSVString(String csvRowString) {
        String[] csvRowArray = csvRowString.split(CSV_DELIMITER_REGEX);
        String name = csvRowArray[0];
        int daysToReminder = Integer.parseInt(csvRowArray[1]);
        return new Category(name, daysToReminder);
    }

    //--------------------------------------------------------------------------
    //  This method serializes the object to a String for a csv row.
    //--------------------------------------------------------------------------
    public String toCSVRowString() {
        return name + CSV_DELIMITER + daysToReminder;
    }

    //--------------------------------------------------------------------------
    //  A getter method for the 'name' variable.
    //--------------------------------------------------------------------------
    public String getName() {
        return name;
    }

    //--------------------------------------------------------------------------
    //  A getter method for the 'daysToReminder' variable.
    //--------------------------------------------------------------------------
    public int getDaysToReminder() {
        return daysToReminder;
    }
}

//******************************************************************************
//  Friend.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This class represents a single friend (a stored contact).
//******************************************************************************
package com.example.friendkeepr;

import androidx.annotation.NonNull;

public final class Friend {

    public static final String CSV_DELIMITER = "|";
    public static final String CSV_DELIMITER_REGEX = "\\|";

    private final String id;
    private final String name;
    private final String category;
    private final String[] phoneNumbers;
    private final int daysToReminder;

    //--------------------------------------------------------------------------
    //  A constructor for the class. Sets 'daysToReminder' to -1, which is used
    //  to indicate that the value should be fetched from the 'Category' object.
    //--------------------------------------------------------------------------
    public Friend(String id, String name, String category, String[] phoneNumbers) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.phoneNumbers = phoneNumbers;
        this.daysToReminder = -1;
    }

    //--------------------------------------------------------------------------
    //  Another constructor for the class that allows you to set a specific
    //  'daysToReminder' value.
    //--------------------------------------------------------------------------
    public Friend(String id, String name, String category, String[] phoneNumbers,
                  int daysToReminder) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.phoneNumbers = phoneNumbers;
        this.daysToReminder = daysToReminder;
    }

    //--------------------------------------------------------------------------
    //  A 'toString' method for the class.
    //--------------------------------------------------------------------------
    @NonNull
    public String toString() {
        return "ID: " + id + " Name: " + name + " Category: " + category;
    }

    //--------------------------------------------------------------------------
    //  This static constructor returns a 'Friend' object from a csv line as a
    //  String.
    //--------------------------------------------------------------------------
    public static Friend fromCSVString(String csvRowString) {
        String[] csvRowArray = csvRowString.split(Friend.CSV_DELIMITER_REGEX);
        String id = csvRowArray[0];
        String name = csvRowArray[1];
        String category = csvRowArray[2];
        String[] phoneNumbers = csvRowArray[3].split(",");
        int daysToReminder = Integer.parseInt(csvRowArray[4]);

        return new Friend(id, name, category, phoneNumbers, daysToReminder);
    }

    //--------------------------------------------------------------------------
    //  This method returns a String that can be used to store the 'Friend'
    //  object in a csv line.
    //--------------------------------------------------------------------------
    public String toCSVRowString() {
        String phoneNumbersString = String.join(",", phoneNumbers);
        return id + CSV_DELIMITER + name + CSV_DELIMITER + category + CSV_DELIMITER +
                phoneNumbersString + CSV_DELIMITER + daysToReminder;
    }

    //--------------------------------------------------------------------------
    //  A getter for the 'id' variable.
    //--------------------------------------------------------------------------
    public String getId() {
        return id;
    }

    //--------------------------------------------------------------------------
    //  A getter for the 'name' variable.
    //--------------------------------------------------------------------------
    public String getName() {
        return name;
    }

    //--------------------------------------------------------------------------
    //  A getter for the 'category' variable.
    //--------------------------------------------------------------------------
    public String getCategory() {
        return category;
    }

    //--------------------------------------------------------------------------
    //  A getter for the  'daysToReminder' variable.
    //--------------------------------------------------------------------------
    public int getDaysToReminder() {
        return daysToReminder;
    }

    //--------------------------------------------------------------------------
    //  A getter for the  'phoneNumbers' variable.
    //--------------------------------------------------------------------------
    public String[] getPhoneNumbers() {
        return phoneNumbers;
    }

    //--------------------------------------------------------------------------
    //  This method returns a new 'Friend' instance with the 'category' changed.
    //--------------------------------------------------------------------------
    public Friend withCategoryChanged(String category) {
        return new Friend(id, name, category, phoneNumbers, daysToReminder);
    }
}

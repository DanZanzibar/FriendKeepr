//******************************************************************************
//  AllCategories.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This class is a 'Collections' type object for the 'Category' class.
//******************************************************************************
package com.example.friendkeepr;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

public class AllCategories {

    public static final int DEFAULT_DAYS_TO_REMINDER = 30;

    private final Category[] categoriesArray;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public AllCategories(Category[] categoriesArray) {
        this.categoriesArray = categoriesArray;
    }

    //--------------------------------------------------------------------------
    //  A static constructor that returns an instance containing only the
    //  "Custom" 'Category'.
    //--------------------------------------------------------------------------
    public static AllCategories fromDefault() {
        Category custom = new Category("Custom", AllCategories.DEFAULT_DAYS_TO_REMINDER);

        return new AllCategories(new Category[] {custom});
    }

    //--------------------------------------------------------------------------
    //  A static constructor that takes a csv string.
    //--------------------------------------------------------------------------
    public static AllCategories fromCSVString(String fileString) {
        String[] csvRows = fileString.split("\n");
        ArrayList<Category> categoriesArrayList = new ArrayList<>();

        for (String row : csvRows)
            categoriesArrayList.add(Category.fromCSVString(row));

        Category[] categoriesArray = categoriesArrayList.toArray(new Category[0]);

        return new AllCategories(categoriesArray);
    }

    //--------------------------------------------------------------------------
    //  This method serializes the class into a String that can be stored as a
    //  csv file.
    //--------------------------------------------------------------------------
    public String toCSVString() {
        StringBuilder fileString = new StringBuilder();

        for (int i = 0; i < categoriesArray.length; i++) {
            fileString.append(categoriesArray[i].toCSVRowString());
            if (i != categoriesArray.length - 1)
                fileString.append("\n");
        }

        return fileString.toString();
    }

    //--------------------------------------------------------------------------
    //  This method return the 'Category' at a given index.
    //--------------------------------------------------------------------------
    public Category getCategoryAtIndex(int index) {
        return categoriesArray[index];
    }

    //--------------------------------------------------------------------------
    //  This method returns the number of 'Category' objects contained in the
    //  instance.
    //--------------------------------------------------------------------------
    public int size() {
        return categoriesArray.length;
    }

    //--------------------------------------------------------------------------
    //  This method returns an array of the names for each 'Category'.
    //--------------------------------------------------------------------------
    public String[] getNamesArray() {
        String[] names = new String[categoriesArray.length];

        for (int i = 0; i < categoriesArray.length; i++)
            names[i] = categoriesArray[i].getName();

        return names;
    }

    //--------------------------------------------------------------------------
    //  This method returns the name of the 'Category' at a given index.
    //--------------------------------------------------------------------------
    public String getNameAtIndex(int index) {
        return categoriesArray[index].getName();
    }

    //--------------------------------------------------------------------------
    //  This method returns the index of the 'Category' with the given name.
    //--------------------------------------------------------------------------
    public int getIndexForName(String categoryName) {
        int index = -1;

        for (int i = 0; i < categoriesArray.length; i++)
            if (categoriesArray[i].getName().equals(categoryName)) {
                index = i;
                break;
            }

        return index;
    }

    //--------------------------------------------------------------------------
    //  This method return the 'daysToReminder' variable value of the 'Category'
    //  at the given index.
    //--------------------------------------------------------------------------
    public int getDaysToReminderAtIndex(int index) {
        return categoriesArray[index].getDaysToReminder();
    }

    //--------------------------------------------------------------------------
    //  This method returns the 'daysToReminder' variable value of the
    //  'Category' with the given name.
    //--------------------------------------------------------------------------
    public int getDaysToReminderForName(String categoryName) {
        int days = -1;

        for (Category category : categoriesArray) {
            if (category.getName().equals(categoryName)) {
                days = category.getDaysToReminder();
                break;
            }
        }

        return days;
    }

    //--------------------------------------------------------------------------
    //  This method returns a boolean representing if a 'Category' with a given
    //  name already exists in the instance.
    //--------------------------------------------------------------------------
    public boolean containsCategoryName(String categoryName) {
        boolean isContained = false;
        for (String name : getNamesArray())
            if (name.equals(categoryName)) {
                isContained = true;
                break;
            }

        return isContained;
    }

    //--------------------------------------------------------------------------
    //  This factory method returns a new instance of the class with the
    //  'Category' at the given index replaced with the given new 'Category'.
    //--------------------------------------------------------------------------
    public AllCategories withReplacedCategory(Category newCategory, int index) {
        Category[] newArray = new Category[categoriesArray.length];
        System.arraycopy(categoriesArray, 0, newArray, 0, newArray.length);
        newArray[index] = newCategory;

        return new AllCategories(newArray);
    }

    //--------------------------------------------------------------------------
    //  This factory method returns a new instance of the class with a new
    //  'Category' added.
    //--------------------------------------------------------------------------
    public AllCategories withAddedCategory(Category newCategory) {
        Category[] newCategoriesArray = new Category[categoriesArray.length + 1];
        for (int i = 0; i < newCategoriesArray.length; i++) {
            if (i == newCategoriesArray.length - 1)
                newCategoriesArray[i] = newCategory;
            else
                newCategoriesArray[i] = categoriesArray[i];
        }

        return new AllCategories(newCategoriesArray);
    }

    //--------------------------------------------------------------------------
    //  This factory method returns a new instance of the class with a given
    //  'Category' removed.
    //--------------------------------------------------------------------------
    public AllCategories withCategoryRemoved(Category category) {
        AllCategories newAllCategories;

        if (containsCategoryName(category.getName())) {
            Category[] newCategoriesArray = new Category[categoriesArray.length - 1];
            for (int i = 0, j = 0; i < categoriesArray.length; i++)
                if (!categoriesArray[i].getName().equals(category.getName())) {
                    newCategoriesArray[j] = categoriesArray[i];
                    j++;
                }

            newAllCategories = new AllCategories(newCategoriesArray);
        }
        else
            newAllCategories = this;

        return newAllCategories;
    }

    //--------------------------------------------------------------------------
    //  This method validates both potential 'name' and 'daysToReminder'
    //  variables. The 'name' must be unique, and the daysToReminder a valid,
    //  positive integer. If either if not, the method generates a Toast message
    //  for the user.
    //--------------------------------------------------------------------------
    public boolean validateNewCategoryInputs(Context context, String name,
                                             String daysToReminderString) {
        boolean isValid = false;

        if (containsCategoryName(name))
            Toast.makeText(context, R.string.category_name_is_duplicate,
                    Toast.LENGTH_LONG).show();

        else {
            try {
                Integer.parseInt(daysToReminderString);
                isValid = true;
            } catch (NumberFormatException e) {
                Toast.makeText(context, R.string.bad_days_input_for_new_category,
                        Toast.LENGTH_LONG).show();
            }
        }

        return isValid;
    }

    //--------------------------------------------------------------------------
    //  This is an overloaded version of the previous method that provides a
    //  parameter for allowing a specific, duplicate name to be used. It is used
    //  when a user changes the 'daysToReminder' value in the app since the
    //  duplicate name will be removed before the new 'Category' is added.
    //--------------------------------------------------------------------------
    public boolean validateNewCategoryInputs(Context context, String name,
                                             String daysToReminderString, String allowedName) {
        if (name.equals(allowedName)) {
            boolean isValid = false;
            try {
                Integer.parseInt(daysToReminderString);
                isValid = true;
            } catch (NumberFormatException e) {
                Toast.makeText(context, R.string.bad_days_input_for_new_category,
                        Toast.LENGTH_LONG).show();
            }

            return isValid;
        }
        else
            return validateNewCategoryInputs(context, name, daysToReminderString);
    }
}

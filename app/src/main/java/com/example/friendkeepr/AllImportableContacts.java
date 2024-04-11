//******************************************************************************
//  AllImportableContacts.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This class is a 'Collections' type object for the 'ImportableContact' class.
//******************************************************************************
package com.example.friendkeepr;

public class AllImportableContacts {

    private final ImportableContact[] contactsArray;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public AllImportableContacts(ImportableContact[] contacts) {
        this.contactsArray = contacts;
    }

    //--------------------------------------------------------------------------
    //  This method returns the 'name' of the 'ImportableContact' at the given
    //  index.
    //--------------------------------------------------------------------------
    public String getNameAtIndex(int index) {
        return contactsArray[index].getName();
    }

    //--------------------------------------------------------------------------
    //  This method returns the 'id' of the 'ImportableContact' at the given
    //  index.
    //--------------------------------------------------------------------------
    public String getIdAtIndex(int index) {
        return contactsArray[index].getId();
    }

    //--------------------------------------------------------------------------
    //  This method returns the 'phoneNumbers' of the 'ImportableContact' at the
    //  given index.
    //--------------------------------------------------------------------------
    public String[] getPhoneNumbersAtIndex(int index) {
        return contactsArray[index].getPhoneNumbers();
    }

    //--------------------------------------------------------------------------
    //  This method returns the number of 'ImportableContact' objects contained
    //  in the instance.
    //--------------------------------------------------------------------------
    public int size() {
        return contactsArray.length;
    }
}

//******************************************************************************
//  ImportableContact.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This class represents a contact in the phone's "Contacts" app that may be
//  imported to become a 'Friend'.
//******************************************************************************
package com.example.friendkeepr;

public class ImportableContact {

    private final String id;
    private final String name;
    private final String[] phoneNumbers;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public ImportableContact(String id, String name, String[] phoneNumbers) {
        this.id = id;
        this.name = name;
        this.phoneNumbers = phoneNumbers;
    }

    //--------------------------------------------------------------------------
    //  A getter method for the 'id' variable.
    //--------------------------------------------------------------------------
    public String getId() {
        return id;
    }

    //--------------------------------------------------------------------------
    //  A getter method for the 'name' variable.
    //--------------------------------------------------------------------------
    public String getName() {
        return name;
    }

    //--------------------------------------------------------------------------
    //  A getter method for the 'phoneNumbers' variable.
    //--------------------------------------------------------------------------
    public String[] getPhoneNumbers() {
        return phoneNumbers;
    }
}

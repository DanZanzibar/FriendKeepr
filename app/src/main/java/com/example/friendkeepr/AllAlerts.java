//******************************************************************************
//  AllAlerts.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This class is a 'Collections' type object for the 'Alert' class.
//******************************************************************************
package com.example.friendkeepr;

public class AllAlerts {

    private final Alert[] alertArray;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public AllAlerts(Alert[] alertArray) {
        this.alertArray = alertArray;
    }

    //--------------------------------------------------------------------------
    //  This method returns the 'Alert' object at the given index.
    //--------------------------------------------------------------------------
    public Alert getAlertAtIndex(int index) {
        return alertArray[index];
    }

    //--------------------------------------------------------------------------
    //  This method returns the number of 'Alert' objects contained in the
    //  instance.
    //--------------------------------------------------------------------------
    public int size() {
        return alertArray.length;
    }
}

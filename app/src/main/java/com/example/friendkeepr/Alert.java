//******************************************************************************
//  Alert.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This class represents an overdue contact needed for a given 'Friend'.
//******************************************************************************
package com.example.friendkeepr;

import java.time.Period;

public class Alert {

    public static final String MIN_CALL_SECONDS = "1";

    private final Friend friend;
    private final Period periodOverdue;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public Alert(Friend friend, Period periodSinceLastContact) {
        this.friend = friend;
        this.periodOverdue = periodSinceLastContact;
    }

    //--------------------------------------------------------------------------
    //  A getter method for the 'friend' variable.
    //--------------------------------------------------------------------------
    public Friend getFriend() {
        return friend;
    }

    //--------------------------------------------------------------------------
    //  A method to return the period overdue as a human readable String, such
    //  as "1 year, 10 months and 3 days".
    //--------------------------------------------------------------------------
    public String periodToString() {
        String str = "";

        int years = periodOverdue.getYears();
        int months = periodOverdue.getMonths();
        int days = periodOverdue.getDays();

	// If over ten years, just say so.
        if (years >= 10)
            str = "more than 10 years";
	
        else if (years == 0 && months == 0 && days == 0)
            str = "0 days";

	// Construct years, months, and days with the appropriate plural if
	// needed, and neglect 0 years, months, or days if appropriate.
        else {
            if (years > 0) {
                str += years + " year";
                if (years > 1)
                    str += "s";

                if (months > 0 && days > 0)
                    str += ", ";
                else if (months > 0 || days > 0)
                    str += " and ";
            }
            if (months > 0) {
                str += months + " month";
                if (months > 1)
                    str += "s";

                if (days > 0)
                    str += " and ";
            }
            if (days > 0) {
                str += days + " day";
                if (days > 1)
                    str += "s";
            }
        }

        return str;
    }
}

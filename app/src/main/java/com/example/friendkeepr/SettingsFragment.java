//******************************************************************************
//  SettingsFragment.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This 'Fragment' is responsible for housing the settings and uses the
//  'Preference' class to create options. 
//******************************************************************************
package com.example.friendkeepr;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    //--------------------------------------------------------------------------
    //  This method is responsible for creating the 'Preference' object that
    //  represents a single option in the settings.
    //--------------------------------------------------------------------------
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        Preference clearFriendsPref = findPreference("clear_friends_preference");
        if (clearFriendsPref != null)
            clearFriendsPref.setOnPreferenceClickListener(p -> clearFriendsData());
    }

    //--------------------------------------------------------------------------
    //  This helper method is invoked when the corresponding 'Preference' is
    //  clicked.
    //--------------------------------------------------------------------------
    private boolean clearFriendsData() {
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.getAppData().clearAllFriends();

        return true;
    }
}

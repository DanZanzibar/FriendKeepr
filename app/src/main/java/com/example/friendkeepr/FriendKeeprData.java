//******************************************************************************
//  FriendKeeprData.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This class facilitates all access to the stored data for the app. There
//  should only be one instance of this.
//******************************************************************************
package com.example.friendkeepr;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.io.FileNotFoundException;

public class FriendKeeprData {

    public static final String CATEGORIES_PREF = "categories";
    public static final String SHARED_PREF_FILE = "com.example.friendkeepr.sharedPreferences";
    public static final String FRIENDS_CSV_FILE = "friends.csv";

    private final SharedPreferences sharedPreferences;
    private final InternalFileStorageManager friendsStorageManager;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public FriendKeeprData(Context context) {
        this.sharedPreferences = context.getSharedPreferences(
                SHARED_PREF_FILE, Context.MODE_PRIVATE);
        friendsStorageManager = new InternalFileStorageManager(
                new File(context.getFilesDir(), FRIENDS_CSV_FILE));
    }

    //--------------------------------------------------------------------------
    //  This method returns the stored 'AllCategories' data.
    //--------------------------------------------------------------------------
    public AllCategories getCategories() {
        String categoriesCSVFileStr = sharedPreferences.getString(CATEGORIES_PREF, "");
        AllCategories categories;

	// If there is no data, create a 'AllCategories' object containing only
	// the "Custom" category.
        if (!categoriesCSVFileStr.equals(""))
            categories = AllCategories.fromCSVString(categoriesCSVFileStr);
        else
            categories = AllCategories.fromDefault();

        return categories;
    }

    //--------------------------------------------------------------------------
    //  This method stores an 'AllCategories' object in SharedPreferences.
    //--------------------------------------------------------------------------
    public void saveCategories(AllCategories categories) {
        saveStringInSharedPreferences(CATEGORIES_PREF, categories.toCSVString());
    }

    //--------------------------------------------------------------------------
    //  This method returns the stored 'AllFriends' data.
    //--------------------------------------------------------------------------
    public AllFriends getAllFriends() {
        AllFriends friends;

	// If no file currently exists, return an empty 'AllFriends' object.
        try {
            friends = AllFriends.fromCSVString(friendsStorageManager.readFromFile());
        } catch (FileNotFoundException e) {
            friends = new AllFriends(new Friend[0]);
        }

        return friends;
    }

    //--------------------------------------------------------------------------
    //  This method stores an 'AllFriends' object in an internal file.
    //--------------------------------------------------------------------------
    public void saveAllFriends(AllFriends friends) {
        friendsStorageManager.writeToFile(friends.toCSVString());
    }

    //--------------------------------------------------------------------------
    //  This method clears the stored 'AllFriends' data.
    //--------------------------------------------------------------------------
    public void clearAllFriends() {
        friendsStorageManager.clearFile();
    }

    //--------------------------------------------------------------------------
    //  This helper method handles storing a String in SharedPreferences.
    //--------------------------------------------------------------------------
    private void saveStringInSharedPreferences(String prefName, String dataStr) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(prefName, dataStr);
        editor.apply();
    }
}

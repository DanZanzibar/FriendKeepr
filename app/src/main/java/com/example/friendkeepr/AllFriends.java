//******************************************************************************
//  AllFriends.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This class represents a collection of 'Friend' objects.
//******************************************************************************
package com.example.friendkeepr;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public final class AllFriends implements Iterable<Friend> {

    private final Friend[] friendsArray;

    //--------------------------------------------------------------------------
    //  A constructor for the class that takes an array of 'Friend' objects.
    //--------------------------------------------------------------------------
    public AllFriends(Friend[] friendsArray) {
        this.friendsArray = friendsArray;
        sortFriendsByName();
    }

    //--------------------------------------------------------------------------
    //  A 'toString' method for the class.
    //--------------------------------------------------------------------------
    @NonNull
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (Friend friend : friendsArray)
            str.append(friend.toString()).append("\n");

        return str.toString();
    }

    //--------------------------------------------------------------------------
    //  A 'iterator' method to allow for the use of a 'for each' loop.
    //--------------------------------------------------------------------------
    @NonNull
    @Override
    public Iterator<Friend> iterator() {
        List<Friend> friendsList = Arrays.asList(friendsArray);

        return friendsList.iterator();
    }

    //--------------------------------------------------------------------------
    //  A static constructor that takes csv data as a String.
    //--------------------------------------------------------------------------
    public static AllFriends fromCSVString(String csvString) {
        String[] csvRows = csvString.split("\n");
        ArrayList<Friend> friendArrayList = new ArrayList<>();

        for (String row : csvRows)
            if (!row.equals(""))
                friendArrayList.add(Friend.fromCSVString(row));

        Friend[] friendsArray = friendArrayList.toArray(new Friend[0]);

        return new AllFriends(friendsArray);
    }

    //--------------------------------------------------------------------------
    //  This method return a String to be written to a csv file.
    //--------------------------------------------------------------------------
    public String toCSVString() {
        StringBuilder fileString = new StringBuilder();

        for (int i = 0; i < friendsArray.length; i++) {
            fileString.append(friendsArray[i].toCSVRowString());
            if (i != friendsArray.length - 1)
                fileString.append("\n");
        }

        return fileString.toString();
    }

    //--------------------------------------------------------------------------
    //  This method returns a String 
    //--------------------------------------------------------------------------
    public String[] getIdsArray() {
        String[] idsArray = new String[friendsArray.length];

        for (int i = 0; i < friendsArray.length; i++)
            idsArray[i] = friendsArray[i].getId();

        return idsArray;
    }

    //--------------------------------------------------------------------------
    //  This method returns the 'Friend' at the given index.
    //--------------------------------------------------------------------------
    public Friend getFriendAtIndex(int index) {
        return friendsArray[index];
    }

    //--------------------------------------------------------------------------
    //  This method returns the 'name' of the 'Friend' at the given index.
    //--------------------------------------------------------------------------
    public String getNameAtIndex(int index) {
        return friendsArray[index].getName();
    }

    //--------------------------------------------------------------------------
    //  This method returns the 'id' of the 'Friend' at the given index.
    //--------------------------------------------------------------------------
    public String getIdAtIndex(int index) {
        return friendsArray[index].getId();
    }

    //--------------------------------------------------------------------------
    //  This method return the 'categoryName' of the 'Friend' at the given
    //  index.
    //--------------------------------------------------------------------------
    public String getCategoryNameAtIndex(int index) {
        return friendsArray[index].getCategory();
    }

    //--------------------------------------------------------------------------
    //  This factory method returns a new instance of the class with the
    //  'categoryName' of all 'Friend' objects with a given value to a new one.
    //--------------------------------------------------------------------------
    public AllFriends withCategoryNameChanged(String oldCategoryName, String newCategoryName) {
        List<Friend> newFriends = new ArrayList<>();

        for (Friend friend : friendsArray) {
            if (friend.getCategory().equals(oldCategoryName))
                newFriends.add(friend.withCategoryChanged(newCategoryName));
            else
                newFriends.add(friend);
        }

        return new AllFriends(newFriends.toArray(new Friend[0]));
    }

    //--------------------------------------------------------------------------
    //  This factory method returns a new instance of the class with added
    //  'Friend' objects.
    //--------------------------------------------------------------------------
    public AllFriends withAddedFriends(Friend[] addedFriends) {
        Friend[] newFriends = Arrays.copyOf(
                friendsArray, friendsArray.length + addedFriends.length);
        System.arraycopy(
                addedFriends, 0, newFriends, friendsArray.length, addedFriends.length);

        AllFriends newAllFriends = new AllFriends(newFriends);
        newAllFriends.sortFriendsByName();

        return newAllFriends;
    }

    //--------------------------------------------------------------------------
    //  This is an overloaded version of the previous method allowing a
    //  'List<Friend>' object to be passed as the argument.
    //--------------------------------------------------------------------------
    public AllFriends withAddedFriends(List<Friend> addedFriends) {
        Friend[] addedFriendsArray = addedFriends.toArray(new Friend[0]);

        return withAddedFriends(addedFriendsArray);
    }

    //--------------------------------------------------------------------------
    //  This factory method returns a new instance of the class containing only
    //  'Friend' objects with a given 'categoryName'.
    //--------------------------------------------------------------------------
    public AllFriends withGivenCategory(String categoryName) {
        List<Friend> friendsWithCategory = new ArrayList<>();

        for (Friend friend : friendsArray)
            if (friend.getCategory().equals(categoryName))
                friendsWithCategory.add(friend);

        Friend[] friendsWithCategoryArray = friendsWithCategory.toArray(new Friend[0]);

        return new AllFriends(friendsWithCategoryArray);
    }

    //--------------------------------------------------------------------------
    //  This factory method returns a new instance of the class with a given
    //  'Friend' removed.
    //--------------------------------------------------------------------------
    public AllFriends withFriendRemoved(Friend friendToRemove) {
        List<Friend> newFriendsList = new ArrayList<>();

        for (Friend friend : friendsArray)
            if (!friend.getId().equals(friendToRemove.getId()))
                newFriendsList.add(friend);

        Friend[] newFriendsArray = newFriendsList.toArray(new Friend[0]);

        return new AllFriends(newFriendsArray);
    }

    //--------------------------------------------------------------------------
    //  This factory method returns a new instance of the class with a given
    //  'Friend' removed and replaced with another 'Friend'.
    //--------------------------------------------------------------------------
    public AllFriends withFriendReplaced(Friend friendToReplace, Friend newFriend) {
        AllFriends friendsWithRemoval = this.withFriendRemoved(friendToReplace);

        return friendsWithRemoval.withAddedFriends(new Friend[]{newFriend});
    }

    //--------------------------------------------------------------------------
    //  This method returns the number of 'Friend' objects contained in the
    //  instance.
    //--------------------------------------------------------------------------
    public int size() {
        return friendsArray.length;
    }

    //--------------------------------------------------------------------------
    //  This method sorts the 'Friend' objects alphabetically. It is private and
    //  only called in the construction of the class instances to preserve
    //  immutability.
    //--------------------------------------------------------------------------
    private void sortFriendsByName() {
        Arrays.sort(friendsArray, new Comparator<Friend>() {
                    @Override
                    public int compare(Friend friend1, Friend friend2) {
                        return friend1.getName().compareToIgnoreCase(friend2.getName());
                    }
                }
        );
    }
}

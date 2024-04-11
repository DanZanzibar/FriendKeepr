//******************************************************************************
//  FriendsByCategoryViewModel.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This class is the ViewModel for 'FriendsByCategoryFragment'. It stores a
//  'AllFriends' object where all 'Friend' objects are of the same 'Category'.
//******************************************************************************
package com.example.friendkeepr;

import androidx.lifecycle.MutableLiveData;

public class FriendsByCategoryViewModel extends FriendKeeprViewModel{

    private final MutableLiveData<AllFriends> filteredFriends;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public FriendsByCategoryViewModel() {
        filteredFriends = new MutableLiveData<>();
    }

    //--------------------------------------------------------------------------
    //  A getter method for the 'filteredFriends' LiveData object.
    //--------------------------------------------------------------------------
    public MutableLiveData<AllFriends> getFilteredFriendsLiveData() {
        return filteredFriends;
    }

    //--------------------------------------------------------------------------
    //  A getter method for the underlying 'AllFriends' object stored in the
    //  LiveData object.
    //--------------------------------------------------------------------------
    public AllFriends getFilteredFriends() {
        return filteredFriends.getValue();
    }

    //--------------------------------------------------------------------------
    //  An asynchronous setter method for the 'filteredFriends' LiveData object.
    //--------------------------------------------------------------------------
    public void postFilteredFriends(AllFriends filteredFriends) {
        this.filteredFriends.postValue(filteredFriends);
    }
}

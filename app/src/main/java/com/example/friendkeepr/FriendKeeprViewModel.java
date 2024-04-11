//******************************************************************************
//  FriendKeeprViewModel.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This abstract class is extended for all the ViewModels in the app. It
//  provides an 'isLoading' MutableLivedata<Boolean> used to indicate when the
//  data is being updated asynchronously.
//******************************************************************************
package com.example.friendkeepr;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public abstract class FriendKeeprViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isLoading;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public FriendKeeprViewModel() {
        isLoading = new MutableLiveData<>(false);
    }

    //--------------------------------------------------------------------------
    //  A getter for the 'isLoading' LiveData object.
    //--------------------------------------------------------------------------
    public MutableLiveData<Boolean> getIsLoadingLiveData() {
        return isLoading;
    }

    //--------------------------------------------------------------------------
    //  A setter for the 'isLoading' LiveData object's underlying Boolean value.
    //--------------------------------------------------------------------------
    public void setIsLoading(boolean isLoading) {
        this.isLoading.setValue(isLoading);
    }

    //--------------------------------------------------------------------------
    //  A setter that is meant to be used in a background thread and posts the
    //  new value to be updated in the main thread.
    //--------------------------------------------------------------------------
    public void postIsLoading(boolean isLoading) {
        this.isLoading.postValue(isLoading);
    }
}

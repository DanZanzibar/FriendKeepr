//******************************************************************************
//  CategoriesViewModel.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This class is the 'ViewModel' for 'CategoriesFragment'.
//******************************************************************************
package com.example.friendkeepr;

import androidx.lifecycle.MutableLiveData;

public class CategoriesViewModel extends FriendKeeprViewModel {

    private final MutableLiveData<AllCategories> allCategories;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public CategoriesViewModel() {
        allCategories = new MutableLiveData<>();
    }

    //--------------------------------------------------------------------------
    //  A getter method for the 'allCategories' LiveData object.
    //--------------------------------------------------------------------------
    public MutableLiveData<AllCategories> getAllCategoriesLiveData() {
        return allCategories;
    }

    //--------------------------------------------------------------------------
    //  A getter method for the underlying 'AllCategories' object in the
    //  LiveData object.
    //--------------------------------------------------------------------------
    public AllCategories getAllCategories() {
        return allCategories.getValue();
    }

    //--------------------------------------------------------------------------
    //  An asynchronous setter method for the 'allCategories' LiveData object.
    //--------------------------------------------------------------------------
    public void postAllCategories(AllCategories allCategories) {
        this.allCategories.postValue(allCategories);
    }
}

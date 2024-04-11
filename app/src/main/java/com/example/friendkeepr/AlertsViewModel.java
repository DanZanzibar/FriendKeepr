//******************************************************************************
//  AlertsViewModel.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This class is a ViewModel for 'AlertsFragment' and holds all dynamic data
//  for it.
//******************************************************************************
package com.example.friendkeepr;

import androidx.lifecycle.MutableLiveData;

public class AlertsViewModel extends FriendKeeprViewModel{

    private final MutableLiveData<AllAlerts> allAlerts;
    private final MutableLiveData<AllCategories> allCategories;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public AlertsViewModel() {
        allAlerts = new MutableLiveData<>();
        allCategories = new MutableLiveData<>();
    }

    //--------------------------------------------------------------------------
    //  A getter method for the 'allAlerts' LiveData object.
    //--------------------------------------------------------------------------
    public MutableLiveData<AllAlerts> getAllAlertsLiveData() {
        return allAlerts;
    }

    //--------------------------------------------------------------------------
    //  A getter method for the underlying 'AllAlerts' object contained in the
    //  LiveData object.
    //--------------------------------------------------------------------------
    public AllAlerts getAllAlerts() {
        return allAlerts.getValue();
    }

    //--------------------------------------------------------------------------
    //  An asynchronous setter method for the 'allAlerts' LiveData object.
    //--------------------------------------------------------------------------
    public void postAllAlerts(AllAlerts allAlerts) {
        this.allAlerts.postValue(allAlerts);
    }

    //--------------------------------------------------------------------------
    //  A getter method for the 'allCategories' LiveData object.
    //--------------------------------------------------------------------------
    public MutableLiveData<AllCategories> getAllCategoriesLiveData() {
        return allCategories;
    }

    //--------------------------------------------------------------------------
    //  A getter method for the underlying 'AllCategories' object contained in
    //  the LiveData object.
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

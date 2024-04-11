//******************************************************************************
//  ImportViewModel.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This class is a 'ViewModel' for 'ImportFragment'.
//******************************************************************************
package com.example.friendkeepr;

import androidx.lifecycle.MutableLiveData;

public class ImportViewModel extends FriendsByCategoryViewModel {

    private final MutableLiveData<AllImportableContacts> contacts;
    private final MutableLiveData<AllCategories> categories;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public ImportViewModel() {
        contacts = new MutableLiveData<>();
        categories = new MutableLiveData<>();
    }

    //--------------------------------------------------------------------------
    //  A getter method for the 'contacts' LiveData object.
    //--------------------------------------------------------------------------
    public MutableLiveData<AllImportableContacts> getContactsLiveData() {
        return contacts;
    }

    //--------------------------------------------------------------------------
    //  A getter method for the underlying 'AllImportableContacts' object stored
    //  in the LiveData object.
    //--------------------------------------------------------------------------
    public AllImportableContacts getAllContacts() {
        return contacts.getValue();
    }

    //--------------------------------------------------------------------------
    //  An asynchronous setter method for the 'contacts' LiveData object.
    //--------------------------------------------------------------------------
    public void postAllContacts(AllImportableContacts allContacts) {
        contacts.postValue(allContacts);
    }

    //--------------------------------------------------------------------------
    //  A getter method for the 'categories' LiveData object.
    //--------------------------------------------------------------------------
    public MutableLiveData<AllCategories> getCategoriesLiveData() {
        return categories;
    }

    //--------------------------------------------------------------------------
    //  A getter method for the underlying 'AllCategories' object stored in the
    //  LiveData object.
    //--------------------------------------------------------------------------
    public AllCategories getAllCategories() {
        return categories.getValue();
    }

    //--------------------------------------------------------------------------
    //  An asynchronous setter method for the 'categories' LiveData object.
    //--------------------------------------------------------------------------
    public void postAllCategories(AllCategories allCategories) {
        categories.postValue(allCategories);
    }
}

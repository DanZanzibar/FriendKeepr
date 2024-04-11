//******************************************************************************
//  ImportFragment.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This Fragment is the top level destination for the 'Import' location on the
//  bottom navigation bar. It allows for getting contacts from the system's
//  'Contacts' app and adding them to stored 'Friend's. 
//******************************************************************************
package com.example.friendkeepr;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImportFragment extends FriendKeeprFragment {

    private ImportViewModel viewModel;
    private RecyclerView recyclerView;
    private TextView loadingTextView;
    private Button saveButton;

    //--------------------------------------------------------------------------
    //  This method inflates the correct layout.
    //--------------------------------------------------------------------------
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_import, container, false);
    }

    //--------------------------------------------------------------------------
    //  This method is responsible for setting up the fragment after creation.
    //  It sets up Observers for the ViewModel's LiveData objects and the
    //  OnClickListener for the save button.
    //--------------------------------------------------------------------------
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ImportViewModel.class);

        recyclerView = requireView().findViewById(R.id.contacts_recycler_view);
        loadingTextView = requireView().findViewById(R.id.loading_text_view);

        Observer<Boolean> loadingObserver = this::displayLoadingScreen;
        viewModel.getIsLoadingLiveData().observe(getViewLifecycleOwner(), loadingObserver);

        Observer<AllImportableContacts> contactsObserver = this::generateContactsList;
        viewModel.getContactsLiveData().observe(getViewLifecycleOwner(), contactsObserver);

        Observer<AllCategories> categoriesObserver = this::generateContactsList;
        viewModel.getCategoriesLiveData().observe(getViewLifecycleOwner(), categoriesObserver);

        saveButton = requireView().findViewById(R.id.save_to_friends_button);
        saveButton.setOnClickListener(v -> saveButtonPush());
    }

    //--------------------------------------------------------------------------
    //  This method is responsible for calling 'updateViewModel' asynchronously
    //  when the Fragment is resumed.
    //--------------------------------------------------------------------------
    @Override
    public void onResume() {
        super.onResume();

        Runnable runnable = this::updateViewModel;
        Thread thread = new Thread(runnable);
        viewModel.setIsLoading(true);
        thread.start();
    }

    //--------------------------------------------------------------------------
    //  This method fetches the contacts from the system that are not already
    //  in the stored 'AllFriends' data and posts the data to the ViewModel.
    //  It is meant to be called from a background thread.
    //--------------------------------------------------------------------------
    public void updateViewModel() {
        AllImportableContacts contacts = getImportableContactsFromSystem(
                getAppData().getAllFriends());
        viewModel.postAllContacts(contacts);
        viewModel.postAllCategories(getAppData().getCategories());
        viewModel.postIsLoading(false);
    }

    //--------------------------------------------------------------------------
    //  This method is used as the OnClickListener for the save button. 
    //--------------------------------------------------------------------------
    private void saveButtonPush() {
        RecyclerView recyclerView = requireView().findViewById(R.id.contacts_recycler_view);
        int contactsCount = recyclerView.getChildCount();
        List<Friend> addedFriends = new ArrayList<>();

	// Iterate over each ViewHolder containing a listed contact.
        for (int i = 0; i < contactsCount; i++) {
            ImportRecyclerAdapter.ViewHolder holder = (ImportRecyclerAdapter.ViewHolder) recyclerView
                    .findViewHolderForAdapterPosition(i);
            if (holder != null) {
		
		// If the checkbox is selected, add to 'addedFriends'.
                CheckBox checkBox = holder.getCheckBox();
                if (checkBox.isChecked()) {
                    String categoryName = (String) holder.getCategorySpinner().getSelectedItem();
                    String name = (String) holder.getNameTextView().getText();
                    String id = (String) holder.itemView.getTag(R.id.tag_contact_id);
                    String[] phoneNumbers = (String[]) holder.itemView.getTag(R.id.tag_phone_numbers);
                    addedFriends.add(new Friend(id, name, categoryName, phoneNumbers));
                }
            }
        }

        addFriendsAndUpdateContacts(addedFriends);
    }

    //--------------------------------------------------------------------------
    //  This method is responsible for saving the new friends to storage and
    //  updating the ViewModel asynchronously.
    //--------------------------------------------------------------------------
    private void addFriendsAndUpdateContacts(List<Friend> addedFriends) {
        Runnable runnable = () -> {
            AllFriends newAllFriends = getAppData().getAllFriends().withAddedFriends(addedFriends);
            getAppData().saveAllFriends(newAllFriends);
            updateViewModel();
        };

        Thread thread = new Thread(runnable);
        viewModel.setIsLoading(true);
        thread.start();
    }

    //--------------------------------------------------------------------------
    //  This method is designed to be an Observer of the 'AllImportableContacts'
    //  LiveData object in the ViewModel. It generates the 'RecyclerView' that
    //  lists all 'ImportableContact' objects.
    //--------------------------------------------------------------------------
    private void generateContactsList(AllImportableContacts contacts) {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(new ImportRecyclerAdapter(
                contacts, viewModel.getAllCategories()));
    }

    //--------------------------------------------------------------------------
    //  This method is an overloaded version of the previous method, allowing
    //  for observing the 'AllCategories' LiveData object instead, but
    //  performing the same function.
    //--------------------------------------------------------------------------
    private void generateContactsList(AllCategories categories) {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(new ImportRecyclerAdapter(viewModel.getAllContacts(), categories));
    }

    //--------------------------------------------------------------------------
    //  This method queries the system for contacts in the "Contacts" app and
    //  and returns an 'AllImportableContacts' object from all contacts not
    //  already stored as 'Friend' objects.
    //--------------------------------------------------------------------------
    private AllImportableContacts getImportableContactsFromSystem(AllFriends friends) {
        Cursor cursor = getAllContactsCursor();
        List<String> friendIds = Arrays.asList(friends.getIdsArray());
        List<ImportableContact> contactsList = new ArrayList<>();

	
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int contactIdIndex = cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
                String contactId = cursor.getString(contactIdIndex);

		// While looping through the cursor, add as 'ImportableContact'
		// if not already a 'Friend'.
                if (!friendIds.contains(contactId)) {
                    int nameIndex = cursor.getColumnIndexOrThrow(
                            ContactsContract.Contacts.DISPLAY_NAME);
                    String name = cursor.getString(nameIndex);

                    String[] phoneNumbers = getPhoneNumbers(contactId);

                    contactsList.add(new ImportableContact(contactId, name, phoneNumbers));
                }
            }
            cursor.close();
        }

        return new AllImportableContacts(contactsList.toArray(new ImportableContact[0]));
    }

    //--------------------------------------------------------------------------
    //  This method returns a 'Cursor' object with the ID and name for the
    //  contact.
    //--------------------------------------------------------------------------
    private Cursor getAllContactsCursor() {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1";

        return requireContext().getContentResolver().query(uri, projection, selection, null, null);
    }

    //--------------------------------------------------------------------------
    //  This method performs another 'ContentResolver' query to get the phone
    //  number(s) for a contact.
    //--------------------------------------------------------------------------
    private String[] getPhoneNumbers(String contactId) {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER};
        String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
        String[] selectionArgs = {contactId};

        Cursor cursor = requireContext().getContentResolver().query(
                uri, projection, selection, selectionArgs, null);

        List<String> phoneNumberList = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int phoneNumberIndex = cursor.getColumnIndexOrThrow(
                        ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
                phoneNumberList.add(cursor.getString(phoneNumberIndex));
            }
            cursor.close();
        }

        return phoneNumberList.toArray(new String[0]);
    }

    //--------------------------------------------------------------------------
    //  This method is used as an Observer for the ViewModel's 'isLoading'
    //  LiveData object. It sets the screen to display a 'Loading' screen or
    //  the RecyclerView responsible for listing contacts.
    //--------------------------------------------------------------------------
    private void displayLoadingScreen(Boolean isLoading) {

        if (isLoading) {
            loadingTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);
        }
        else
            fadeInContent(loadingTextView, recyclerView, saveButton);
    }
}

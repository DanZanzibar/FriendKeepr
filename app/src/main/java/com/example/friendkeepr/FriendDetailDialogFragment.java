//******************************************************************************
//  FriendDetailDialogFragment.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This 'DialogFragment' displays a 'Friend' object's details and allows the
//  user to edit them.
//******************************************************************************
package com.example.friendkeepr;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class FriendDetailDialogFragment extends DialogFragment {

    public static final String INDEX_ARG = "index";
    public static final String CATEGORY_ARG = "category";

    private FriendsByCategoryFragment parentFragment;
    private int index;
    private Friend friend;
    private AllCategories allCategories;
    private Spinner categorySpinner;
    private EditText daysEditText;

    //--------------------------------------------------------------------------
    //  This method is responsible for retrieving the correct 'Friend' object
    //  and setting up the UI using 'AlertDialog'.
    //--------------------------------------------------------------------------
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

	// Inflates the custom layout for 'AlertDialog'.
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_friend_detail, null);
        builder.setView(dialogView);

	// Get the index of the 'Friend' from the 'Bundle' and the 'AllFriends'
	// object from the parent Fragment to retrieve to correct 'Friend'.
        Bundle args = getArguments();
        if (args != null)
            index = args.getInt(INDEX_ARG);

        parentFragment = (FriendsByCategoryFragment) requireParentFragment();
        allCategories = parentFragment.getAllCategories();

        AllFriends filteredFriends = parentFragment.getViewModel().getFilteredFriends();
        friend = filteredFriends.getFriendAtIndex(index);

	// Sets up the 'Spinner' for the category selection.
        categorySpinner = dialogView.findViewById(R.id.category_spinner);
        daysEditText = dialogView.findViewById(R.id.days_to_reminder_edittext);

        setEditTextViewHint(friend.getCategory());
        setUpCategoriesSpinner();

        builder.setTitle(friend.getName());

	// Sets up the action buttons.
        builder.setPositiveButton("Save", (dialog, which) -> clickSaveButton());

        builder.setNegativeButton("Cancel", (dialog, which) -> {});

        return builder.create();
    }

    //--------------------------------------------------------------------------
    //  This helper method sets up the correct 'EditText' hints based on the
    //  current data.
    //--------------------------------------------------------------------------
    private void setEditTextViewHint(String categoryName) {
        String hint;

	// If the 'friend.getDaysToReminder()' value is '-1', retrieve the value
	// from the 'Category' object instead.
        if (categoryName.equals("Custom")) {
            if (friend.getDaysToReminder() == -1)
                hint = String.valueOf(AllCategories.DEFAULT_DAYS_TO_REMINDER);
            else
                hint = String.valueOf(friend.getDaysToReminder());
        }
        else
            hint = String.valueOf(allCategories.getDaysToReminderForName(categoryName));

        daysEditText.setHint(hint);

	// Only enable editing the 'daysToReminder' value if in the "Custom"
	// category.
        daysEditText.setEnabled(categoryName.equals("Custom"));
    }

    //--------------------------------------------------------------------------
    //  This helper method sets up the Spinner showing categories and updates
    //  the 'EditText' hints for the days once a new one is selected.
    //--------------------------------------------------------------------------
    private void setUpCategoriesSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                allCategories.getNamesArray()
        );
        categorySpinner.setAdapter(adapter);

        String initialCategoryName = friend.getCategory();

        categorySpinner.setSelection(allCategories.getIndexForName(initialCategoryName));
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

		// Change the hints based on the category selected.
                String categorySelectedName = allCategories.getNameAtIndex(position);
                setEditTextViewHint(categorySelectedName);
                daysEditText.setEnabled(categorySelectedName.equals("Custom"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    //--------------------------------------------------------------------------
    //  This helper method saves the 'Friend' data and updates the appropriate
    //  ViewModel asynchronously.
    //--------------------------------------------------------------------------
    private void clickSaveButton() {
        Runnable runnable = () -> {

	    // Get all the parameters for the new 'Friend' object.
            String id = friend.getId();
            String name = friend.getName();
            String category = categorySpinner.getSelectedItem().toString();
            String[] phoneNumbers = friend.getPhoneNumbers();
	    
            String daysEditTextInput = daysEditText.getText().toString();

	    // If days not entered or a non-custom category, set to '-1'.
            int daysToReminder;
            if (daysEditTextInput.equals("") || !category.equals("Custom"))
                daysToReminder = -1;
            else
                daysToReminder = Integer.parseInt(daysEditText.getText().toString());

            Friend modifiedFriend = new Friend(id, name, category, phoneNumbers, daysToReminder);

	    // Create and save the new 'Friend' object.
            AllFriends allFriends = parentFragment.getAppData().getAllFriends();
            AllFriends newFriends = allFriends.withFriendReplaced(friend, modifiedFriend);

            parentFragment.getAppData().saveAllFriends(newFriends);
            parentFragment.updateViewModel();
        };
        Thread thread = new Thread(runnable);
        parentFragment.getViewModel().setIsLoading(true);
        thread.start();
    }
}

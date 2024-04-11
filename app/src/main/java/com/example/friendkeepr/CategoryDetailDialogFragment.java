//******************************************************************************
//  CategoryDetailDialogFragment.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This 'DialogFragment' is displayed when the user wants to edit a given
//  'Category'.
//******************************************************************************
package com.example.friendkeepr;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class CategoryDetailDialogFragment extends DialogFragment {

    public static final String CATEGORY_INDEX_ARG = "index";

    private CategoriesFragment fragment;
    private AllCategories allCategories;
    private int index;
    private Category category;
    private EditText nameEditText;
    private EditText daysEditText;

    //--------------------------------------------------------------------------
    //  This method retrieves information on the given 'Category' and sets up
    //  the UI using the 'AlertDialog' class.
    //--------------------------------------------------------------------------
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

	// First, get the 'AllCategories' object from the parent's ViewModel.
        fragment = (CategoriesFragment) requireParentFragment();
        allCategories = fragment.getViewModel().getAllCategories();

	// Use the index stored in the 'Bundle' to retrieve teh 'Category'.
        Bundle args = getArguments();
        if (args != null)
            index = args.getInt(CATEGORY_INDEX_ARG);

        category = allCategories.getCategoryAtIndex(index);

	// Inflate the custom layout for 'AlertDialog'.
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_category_detail, null);
        builder.setView(dialogView);

	// Set up the child Views and actions for the buttons.
        nameEditText = dialogView.findViewById(R.id.name_edittext);
        daysEditText = dialogView.findViewById(R.id.days_edittext);

        setUpEditTextHints(dialogView);

        builder.setTitle(category.getName());

        builder.setPositiveButton("Save", (dialog, which) -> saveButtonClick());

        builder.setNegativeButton("Cancel", (dialog, which) -> {});

        return builder.create();
    }

    //--------------------------------------------------------------------------
    //  This helper method sets the appropriate 'hints' for the 'EditText'
    //  Views.
    //--------------------------------------------------------------------------
    private void setUpEditTextHints(View dialogView) {

        String categoryName = category.getName();
        nameEditText.setHint(categoryName);

	// If the "Custom" 'Category', do not show the 'EditText' View for the
	// name.
        if (categoryName.equals("Custom")) {
            nameEditText.setVisibility(View.INVISIBLE);
            dialogView.findViewById(R.id.name_label).setVisibility(View.INVISIBLE);
        }

        int originalDays = category.getDaysToReminder();
        daysEditText.setHint(String.valueOf(originalDays));
    }

    //--------------------------------------------------------------------------
    //  This method is invoked upon a "Save" button click.
    //--------------------------------------------------------------------------
    private void saveButtonClick() {
        String name = nameEditText.getText().toString();

	// If no name was entered, use the original name of the 'Category'.
        if (name.equals(""))
            name = category.getName();

        String daysString = daysEditText.getText().toString();

	// If no number of days was entered, use the original value.
        if (daysString.equals(""))
            daysString = String.valueOf(category.getDaysToReminder());

	// Validate the inputs before saving.
        if (allCategories.validateNewCategoryInputs(
                requireContext(), name, daysString, category.getName()))
            saveModifications(name, daysString);
    }

    //--------------------------------------------------------------------------
    //  This method is a helper method for saving the modified 'Category'.
    //--------------------------------------------------------------------------
    private void saveModifications(String newName, String newDaysToReminderString) {
        Category modifiedCategory = new Category(newName,
                Integer.parseInt(newDaysToReminderString));
        AllCategories newCategories = allCategories
                .withReplacedCategory(modifiedCategory, index);

        FriendKeeprData appData = fragment.getAppData();

	// Once the new 'AllCategories' object is created, store the new data
	// and update the parent's ViewModel asynchronously.
        Runnable runnable = () -> {
            appData.saveCategories(newCategories);

            String originalName = category.getName();

	    // If the name of the 'Category' has changed, update all 'Friend'
	    // objects with the new name.
            if (!newName.equals(originalName)) {
                AllFriends oldFriends = appData.getAllFriends();
                AllFriends newFriends = oldFriends.withCategoryNameChanged(originalName, newName);
                appData.saveAllFriends(newFriends);
            }

            fragment.updateViewModel();
        };
        Thread thread = new Thread(runnable);
        fragment.getViewModel().setIsLoading(true);
        thread.start();
    }
}

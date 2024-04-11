//******************************************************************************
//  NewCategoryDialogFragment.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This 'DialogFragment' is used to add a new 'Category' by the user. It
//  prompts for a name and number of days before reminders.
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

public class NewCategoryDialogFragment extends DialogFragment {

    private CategoriesFragment fragment;

    //--------------------------------------------------------------------------
    //  This method is responsible for inflating the custom layout and uses
    //  'AlertDialog' to build the UI and set the button actions.
    //--------------------------------------------------------------------------
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

	// Gets a reference to the parent 'CategoriesFragment'.
        this.fragment = (CategoriesFragment) requireParentFragment();

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_new_category, null);
        builder.setView(view);

        EditText nameEditText = view.findViewById(R.id.new_category_name_edittext);
        EditText daysEditText = view.findViewById(R.id.new_category_days_edittext);

        builder.setTitle(R.string.new_category_dialog_title);

	// Sets up the buttons.
        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = nameEditText.getText().toString();
            String daysString = daysEditText.getText().toString();

            AllCategories allCategories = fragment.getViewModel().getAllCategories();

            if (allCategories.validateNewCategoryInputs(requireContext(), name, daysString))
                saveNewCategory(name, daysString);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {});

        return builder.create();
    }

    //--------------------------------------------------------------------------
    //  This method is invoked to save the new 'Category' to stored data and
    //  update the 'ViewModel' asynchronously.
    //--------------------------------------------------------------------------
    private void saveNewCategory(String name, String daysString) {
        Category newCategory = new Category(name, Integer.parseInt(daysString));

        Runnable runnable = () -> {
            AllCategories categories = fragment.getViewModel().getAllCategories();
            fragment.getAppData().saveCategories(categories.withAddedCategory(newCategory));
            fragment.updateViewModel();
        };
        Thread thread = new Thread(runnable);
        fragment.getViewModel().setIsLoading(true);
        thread.start();
    }
}

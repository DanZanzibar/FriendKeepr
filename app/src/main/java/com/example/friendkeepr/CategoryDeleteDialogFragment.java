//******************************************************************************
//  CategoryDeleteDialogFragment.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This DialogFragment is for confirming that the user wants to delete a
//  'Category'.
//******************************************************************************
package com.example.friendkeepr;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class CategoryDeleteDialogFragment extends DialogFragment {

    public static final String CATEGORY_INDEX_ARG = "index";

    private CategoriesFragment fragment;
    private AllCategories allCategories;
    private int index;
    private Category category;

    //--------------------------------------------------------------------------
    //  This method retrieves all the necessary data to complete the potential
    //  deletion and uses the 'AlertDialog' class to build the UI and set the
    //  buttons' actions up.
    //--------------------------------------------------------------------------
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

	// It first gets the 'AllCategories' data from the parent's ViewModel.
        fragment = (CategoriesFragment) requireParentFragment();
        allCategories = fragment.getViewModel().getAllCategories();

	// Get the stored index of the 'Category' in question for retrieval
	// from the 'AllCategories' object.
        Bundle args = getArguments();
        if (args != null)
            index = args.getInt(CATEGORY_INDEX_ARG);

        category = allCategories.getCategoryAtIndex(index);

	// Build the dialog using 'AlertDialog'.
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Are you sure you want to delete '" + category.getName() + "'?");

        builder.setPositiveButton("Delete", (dialog, which) -> deleteCategory());
        builder.setNegativeButton("Cancel", (dialog, which) -> {});

        return builder.create();
    }

    //--------------------------------------------------------------------------
    //  This helper method affects the deletion of the 'Category' associated
    //  with this DialogFragment.
    //--------------------------------------------------------------------------
    private void deleteCategory() {
        AllCategories newAllCategories = allCategories.withCategoryRemoved(category);

	// Save the new 'AllCategories', then change any 'Friend' objects with
	// the old 'Category' name stored in them to "Custom". Finally, update
	// the ViewModel. All done asynchronously.
        Runnable runnable = () -> {
            fragment.getAppData().saveCategories(newAllCategories);

            AllFriends newAllFriends = fragment.getAppData().getAllFriends()
                    .withCategoryNameChanged(category.getName(), "Custom");
            fragment.getAppData().saveAllFriends(newAllFriends);

            fragment.updateViewModel();
        };
        Thread thread = new Thread(runnable);
        fragment.getViewModel().setIsLoading(true);
        thread.start();
    }
}

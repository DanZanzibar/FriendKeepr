//******************************************************************************
//  FriendDeleteDialogFragment.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This 'DialogFragment' is used for confirming the user wishes to delete a
//  'Friend' object.
//******************************************************************************
package com.example.friendkeepr;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class FriendDeleteDialogFragment extends DialogFragment {

    public static final String INDEX_ARG = "index";

    private FriendsByCategoryFragment parentFragment;
    private Friend friend;
    private int index;

    //--------------------------------------------------------------------------
    //  This method is responsible for retrieving the 'Friend' data and uses
    //  'AlertDialog' to set up the UI and buttons.
    //--------------------------------------------------------------------------
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

	// First gets the 'AllFriend' object stored in the parent's ViewModel.
        parentFragment = (FriendsByCategoryFragment) requireParentFragment();

	// Gets the index of the 'Friend' object from the 'Bundle' to retrieve
	// the correct 'Friend'.
        Bundle args = getArguments();
        if (args != null)
            index = args.getInt(INDEX_ARG);

        AllFriends filteredFriends = parentFragment.getViewModel().getFilteredFriends();
        friend = filteredFriends.getFriendAtIndex(index);

	// Builds the UI using 'AlertDialog' and sets up the actions for the
	// buttons.
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Are you sure you want to delete '" + friend.getName() + "'?");

        builder.setPositiveButton("Delete", (dialog, which) -> deleteFriend());
        builder.setNegativeButton("Cancel", (dialog, which) -> {});

        return builder.create();
    }

    //--------------------------------------------------------------------------
    //  This helper method removes a 'Friend' from the stored 'AllFriends'
    //  object and updates the parent's ViewModel asynchronously.
    //--------------------------------------------------------------------------
    private void deleteFriend() {
        Runnable runnable = () -> {
            AllFriends currentAllFriends = parentFragment.getAppData().getAllFriends();
            AllFriends newAllFriends = currentAllFriends.withFriendRemoved(friend);
            parentFragment.getAppData().saveAllFriends(newAllFriends);
            parentFragment.updateViewModel();
        };
        Thread thread = new Thread(runnable);
        parentFragment.getViewModel().setIsLoading(true);
        thread.start();
    }
}

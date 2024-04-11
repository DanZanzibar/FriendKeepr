//******************************************************************************
//  FriendsTabAdapter.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This class is a custom 'FragmentStateAdapter' used by a 'TabLayoutManager'.
//******************************************************************************
package com.example.friendkeepr;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FriendsTabAdapter extends FragmentStateAdapter {

    private final AllCategories allCategories;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public FriendsTabAdapter(Fragment fragment, AllCategories allCategories) {
        super(fragment);
        this.allCategories = allCategories;
    }

    //--------------------------------------------------------------------------
    //  This method is responsible for instantiating a
    //  'FriendByCategoryFragment' for each 'Category' in the 'AllCategories'
    //  object.
    //--------------------------------------------------------------------------
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String categoryName = allCategories.getNameAtIndex(position);

        FriendsByCategoryFragment fragment = new FriendsByCategoryFragment();

	// Pass the String value of the 'Category' name to the Fragment.
        Bundle args = new Bundle();
        args.putString(FriendsByCategoryFragment.CATEGORY_NAME_ARG, categoryName);
        fragment.setArguments(args);

        return fragment;
    }

    //--------------------------------------------------------------------------
    //  This method returns the number of tabs (the number of categories). 
    //--------------------------------------------------------------------------
    @Override
    public int getItemCount() {
        return allCategories.size();
    }
}

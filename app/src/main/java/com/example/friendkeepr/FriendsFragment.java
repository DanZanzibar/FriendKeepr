//******************************************************************************
//  FriendsFragment.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This 'Fragment' is responsible for displaying all tabs of the various
//  'FriendsByCategoryFragment' screens.
//******************************************************************************
package com.example.friendkeepr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FriendsFragment extends FriendKeeprFragment {

    private FriendsViewModel viewModel;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private TextView loadingTextView;
    
    //--------------------------------------------------------------------------
    //  This method is responsible for inflating the layout.
    //--------------------------------------------------------------------------
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    //--------------------------------------------------------------------------
    //  This method is responsible for setting up the ViewModel and Observers.
    //--------------------------------------------------------------------------
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(FriendsViewModel.class);

        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.category_tabs);
        loadingTextView = view.findViewById(R.id.loading_text_view);

        Observer<Boolean> loadingObserver = this::displayLoadingScreen;
        viewModel.getIsLoadingLiveData().observe(getViewLifecycleOwner(), loadingObserver);

        Observer<AllCategories> categoriesObserver = this::generateFriendsLists;
        viewModel.getAllCategoriesLiveData().observe(getViewLifecycleOwner(), categoriesObserver);
    }

    //--------------------------------------------------------------------------
    //  This method is responsible for updating the data in the ViewModel
    //  asynchronously.
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
    //  This method fetches the data from storage and posts it to the ViewModel.
    //--------------------------------------------------------------------------
    public void updateViewModel() {
        viewModel.postAllCategories(getAppData().getCategories());
        viewModel.postIsLoading(false);
    }

    //--------------------------------------------------------------------------
    //  A getter method for the ViewModel.
    //--------------------------------------------------------------------------
    public FriendsViewModel getViewModel() {
        return viewModel;
    }

    //--------------------------------------------------------------------------
    //  This method is designed to be an Observer of the 'AllCategories' object
    //  stored in the ViewModel. It instantiates the 'TabLayoutManager', using
    //  a 'FriendsTabAdapter' instance.
    //--------------------------------------------------------------------------
    private void generateFriendsLists(AllCategories allCategories) {
        FriendsTabAdapter tabAdapter = new FriendsTabAdapter(this, allCategories);
        viewPager.setAdapter(tabAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(allCategories.getNameAtIndex(position))
        ).attach();
    }

    //--------------------------------------------------------------------------
    //  This method is designed to be an Observer for the 'isLoading' LiveData
    //  object stored in the ViewModel. It displays a loading screen while
    //  data is being updated.
    //--------------------------------------------------------------------------
    private void displayLoadingScreen(Boolean isLoading) {
        if (isLoading) {
            viewPager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.INVISIBLE);
            loadingTextView.setVisibility(View.VISIBLE);
        }
        else
            fadeInContent(loadingTextView, viewPager, tabLayout);
    }
}

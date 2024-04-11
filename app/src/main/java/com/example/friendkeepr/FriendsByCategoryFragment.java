//******************************************************************************
//  FriendsByCategoryFragment.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This 'Fragment' is responsible for displaying a single tab (based on one
//  'Category') of the parent 'FriendsFragment'.
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FriendsByCategoryFragment extends FriendKeeprFragment {

    public static final String CATEGORY_NAME_ARG = "category";

    private FriendsByCategoryViewModel viewModel;
    private String categoryName;
    private AllCategories allCategories;
    private RecyclerView recyclerView;
    private TextView loadingTextView;

    //--------------------------------------------------------------------------
    //  This method is responsible for inflating the layout.
    //--------------------------------------------------------------------------
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends_by_category, container, false);
    }

    //--------------------------------------------------------------------------
    //  This method is responsible for setting up the ViewModel and the
    //  Observers for the LiveData.
    //--------------------------------------------------------------------------
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(FriendsByCategoryViewModel.class);

	// Get the 'AllCategories' object from the parent 'FriendsFragment'.
        FriendsFragment parentFragment = (FriendsFragment) requireParentFragment();
        allCategories = parentFragment.getViewModel().getAllCategories();

	// Get the category name for this Fragment from the 'Bundle'.
        Bundle args = getArguments();
        if (args != null)
            categoryName = args.getString(CATEGORY_NAME_ARG);

        recyclerView = view.findViewById(R.id.recycler_view);
        loadingTextView = view.findViewById(R.id.loading_text_view);

        Observer<Boolean> loadingObserver = this::displayLoadingScreen;
        viewModel.getIsLoadingLiveData().observe(getViewLifecycleOwner(), loadingObserver);

        Observer<AllFriends> friendsObserver = this::generateFriendsList;
        viewModel.getFilteredFriendsLiveData().observe(getViewLifecycleOwner(), friendsObserver);
    }

    //--------------------------------------------------------------------------
    //  This method is responsible for updating the ViewModel asynchronously.
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
    //  A getter method for the ViewModel.
    //--------------------------------------------------------------------------
    public FriendsByCategoryViewModel getViewModel() {
        return viewModel;
    }

    //--------------------------------------------------------------------------
    //  This method fetches the data from storage to update the ViewModel.
    //  Should be called in a background thread.
    //--------------------------------------------------------------------------
    public void updateViewModel() {
        AllFriends filteredFriends = getAppData().getAllFriends().
                withGivenCategory(categoryName);
        viewModel.postFilteredFriends(filteredFriends);
        viewModel.postIsLoading(false);
    }

    //--------------------------------------------------------------------------
    //  A getter method for the cached 'AllCategories' object.
    //--------------------------------------------------------------------------
    public AllCategories getAllCategories() {
        return allCategories;
    }

    //--------------------------------------------------------------------------
    //  This method is designed to be an Observer of the 'AllFriends' object
    //  stored in the ViewModel representing friends for a particular category.
    //--------------------------------------------------------------------------
    private void generateFriendsList(AllFriends filteredFriends) {
        FriendsRecyclerAdapter adapter = new FriendsRecyclerAdapter(this,
                filteredFriends);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    //--------------------------------------------------------------------------
    //  This method is designed to be an Observer for the 'isLoading' LiveData
    //  object stored in the ViewModel.
    //--------------------------------------------------------------------------
    private void displayLoadingScreen(Boolean isLoading) {
        if (isLoading) {
            recyclerView.setVisibility(View.GONE);
            loadingTextView.setVisibility(View.VISIBLE);
        }
        else
            fadeInContent(loadingTextView, recyclerView);
    }
}

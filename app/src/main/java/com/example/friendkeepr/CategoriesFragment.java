//******************************************************************************
//  CategoriesFragment.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This fragment is the screen that displays and allows for editing the
//  'Category' objects.
//******************************************************************************
package com.example.friendkeepr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoriesFragment extends FriendKeeprFragment {

    private CategoriesViewModel viewModel;
    private RecyclerView recyclerView;
    private Button newCategoryButton;
    private TextView loadingTextView;

    //--------------------------------------------------------------------------
    //  This method is responsible for inflating the layout.
    //--------------------------------------------------------------------------
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    //--------------------------------------------------------------------------
    //  This method is responsible for setting up the ViewMode, Observers, and
    //  onClickListeners.
    //--------------------------------------------------------------------------
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        recyclerView = view.findViewById(R.id.categories_recycler_view);
        loadingTextView = view.findViewById(R.id.loading_text_view);

        newCategoryButton = view.findViewById(R.id.add_new_category_button);
        newCategoryButton.setOnClickListener(this::addNewCategory);

        Observer<Boolean> loadingObserver = this::displayLoadingScreen;
        viewModel.getIsLoadingLiveData().observe(getViewLifecycleOwner(), loadingObserver);

        Observer<AllCategories> categoriesObserver = this::generateCategoriesList;
        viewModel.getAllCategoriesLiveData().observe(getViewLifecycleOwner(), categoriesObserver);
    }

    //--------------------------------------------------------------------------
    //  This method is responsible for updating the data in the ViewModel.
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
    //  A getter method for the 'viewModel' variable.
    //--------------------------------------------------------------------------
    public CategoriesViewModel getViewModel() {
        return viewModel;
    }

    //--------------------------------------------------------------------------
    //  This method fetches the data from storage and posts the values to the
    //  ViewModel. Should be called in a background thread.
    //--------------------------------------------------------------------------
    public void updateViewModel() {
        viewModel.postAllCategories(getAppData().getCategories());
        viewModel.postIsLoading(false);
    }

    //--------------------------------------------------------------------------
    //  This method is designed to be an OnClickListener for the
    //  'newCategoryButton'. It instantiates an instance of
    //  'NewCategoryDialogFragment'.
    //--------------------------------------------------------------------------
    private void addNewCategory(View view) {
        NewCategoryDialogFragment dialogFragment = new NewCategoryDialogFragment();
        dialogFragment.show(getChildFragmentManager(), "new_category");
    }

    //--------------------------------------------------------------------------
    //  This method is designed to be an Observer for the 'AllCategories' object
    //  stored in the ViewModel and populates the RecyclerView that displays
    //  the categories.
    //--------------------------------------------------------------------------
    private void generateCategoriesList(AllCategories allCategories) {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(new CategoriesRecyclerAdapter(this, allCategories));
    }

    //--------------------------------------------------------------------------
    //  This method is designed to be an Observer for the 'isLoading' object
    //  stored in the ViewModel and displays the loading screen when data is
    //  being updated.
    //--------------------------------------------------------------------------
    private void displayLoadingScreen(Boolean isLoading) {
        if (isLoading) {
            recyclerView.setVisibility(View.GONE);
            newCategoryButton.setVisibility(View.GONE);
            loadingTextView.setVisibility(View.VISIBLE);
        }
        else
            fadeInContent(loadingTextView, recyclerView, newCategoryButton);
    }
}

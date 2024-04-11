//******************************************************************************
//  CategoriesRecyclerAdapter.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This class is a custom 'RecyclerAdapter' for displaying the 'AllCategories'
//  object.
//******************************************************************************
package com.example.friendkeepr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoriesRecyclerAdapter extends RecyclerView.Adapter<CategoriesRecyclerAdapter.ViewHolder> {

    private final CategoriesFragment fragment;
    private final AllCategories allCategories;

    //--------------------------------------------------------------------------
    //  This nested class is a custom 'ViewHolder' representing each 'Category'.
    //--------------------------------------------------------------------------
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;
        private final TextView daysTextView;
        private final ImageButton deleteButton;
        private final ImageButton editButton;

	//--------------------------------------------------------------------------
	//  A constructor for the nested class that gets references for the various
	//  child Views.
	//--------------------------------------------------------------------------
        public ViewHolder(View view) {
            super(view);

            nameTextView = view.findViewById(R.id.category_name_text_view);
            daysTextView = view.findViewById(R.id.category_days_textview);
            deleteButton = view.findViewById(R.id.delete_button_view);
            editButton = view.findViewById(R.id.edit_button_view);
        }

	//--------------------------------------------------------------------------
	//  A getter method for the 'nameTextView' variable of the nested class.
	//--------------------------------------------------------------------------
        public TextView getNameTextView() {
            return nameTextView;
        }

	//--------------------------------------------------------------------------
	//  A getter method for the 'daysTextView' variable of the nested class.
	//--------------------------------------------------------------------------
        public TextView getDaysTextView() {
            return daysTextView;
        }

	//--------------------------------------------------------------------------
	//  A getter method for the 'deleteButton' variable of the nested class.
	//--------------------------------------------------------------------------
        public ImageButton getDeleteButton() {
            return deleteButton;
        }

	//--------------------------------------------------------------------------
	//  A getter method for the 'editButton' variable of the nested class.
	//--------------------------------------------------------------------------
        public ImageButton getEditButton() {
            return editButton;
        }
    }

    //--------------------------------------------------------------------------
    //  A constructor for the Adapter.
    //--------------------------------------------------------------------------
    public CategoriesRecyclerAdapter(CategoriesFragment fragment, AllCategories allCategories) {
        this.fragment = fragment;
        this.allCategories = allCategories;
    }

    //--------------------------------------------------------------------------
    //  This method is responsible for inflating the layout of each
    //  'ViewHolder'.
    //--------------------------------------------------------------------------
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_item, parent, false);

        return new ViewHolder(view);
    }

    //--------------------------------------------------------------------------
    //  This method is responsible for binding data to the various child Views
    //  of the 'ViewHolder'.
    //--------------------------------------------------------------------------
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String categoryName = allCategories.getNameAtIndex(position);
        holder.getNameTextView().setText(categoryName);
        String daysToReminderString = "Days to reminder: " +
                allCategories.getDaysToReminderAtIndex(position);
        holder.getDaysTextView().setText(daysToReminderString);

        holder.getDeleteButton().setOnClickListener(v -> clickDeleteButton(position));

        holder.getEditButton().setOnClickListener(v -> clickEditButton(position));

	// If the category is "Custom", get rid of the 'deleteButton'.
        if (categoryName.equals("Custom"))
            holder.getDeleteButton().setVisibility(View.GONE);
    }

    //--------------------------------------------------------------------------
    //  This method returns the number of items in the 'RecyclerView'.
    //--------------------------------------------------------------------------
    @Override
    public int getItemCount() {
        return allCategories.size();
    }

    //--------------------------------------------------------------------------
    //  This method is designed to be the 'OnClickListener' for the
    //  'deleteButton'. It instantiates the 'CategoryDeleteDialogFragment' and
    //  passes it the category index.
    //--------------------------------------------------------------------------
    private void clickDeleteButton(int position) {
        Bundle args = new Bundle();
        args.putInt(CategoryDeleteDialogFragment.CATEGORY_INDEX_ARG, position);

        CategoryDeleteDialogFragment dialogFragment = new CategoryDeleteDialogFragment();
        dialogFragment.setArguments(args);
        dialogFragment.show(fragment.getChildFragmentManager(), "category_delete");
    }

    //--------------------------------------------------------------------------
    //  This method is designed to be the 'OnClickListener' for the
    //  'editButton'. It instantiates the 'CategoryDetailDialogFragment' and
    //  passes it the category index.
    //--------------------------------------------------------------------------    
    private void clickEditButton(int position) {
        Bundle args = new Bundle();
        args.putInt(CategoryDetailDialogFragment.CATEGORY_INDEX_ARG, position);

        CategoryDetailDialogFragment dialogFragment = new CategoryDetailDialogFragment();
        dialogFragment.setArguments(args);
        dialogFragment.show(fragment.getChildFragmentManager(), "category_edit");
    }
}

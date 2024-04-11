//******************************************************************************
//  ImportRecyclerAdapter.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This class is a custom 'RecyclerAdapter' for displaying an
//  'AllImportableContacts' object. 
//******************************************************************************
package com.example.friendkeepr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImportRecyclerAdapter extends RecyclerView.Adapter<ImportRecyclerAdapter.ViewHolder> {

    private final AllImportableContacts contacts;
    private final AllCategories categories;

    //--------------------------------------------------------------------------
    //  This nested class is a custom 'ViewHolder' for displaying a single
    //  'ImportableContact' object.
    //--------------------------------------------------------------------------
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox checkBox;
        private final TextView nameTextView;
        private final Spinner categorySpinner;

	//--------------------------------------------------------------------------
	//  A constructor for the nested class that sets up references to the
	//  various child Views.
	//--------------------------------------------------------------------------
        public ViewHolder(View view) {
            super(view);

            checkBox = view.findViewById(R.id.checkbox);
            nameTextView = view.findViewById(R.id.contact_name_textview);
            categorySpinner = view.findViewById(R.id.category_spinner);
        }

	//--------------------------------------------------------------------------
	//  A getter method for the 'checkBox' variable.
	//--------------------------------------------------------------------------
        public CheckBox getCheckBox() {
            return checkBox;
        }

	//--------------------------------------------------------------------------
	//  A getter method for the 'nameTextView' variable.
	//--------------------------------------------------------------------------
        public TextView getNameTextView() {
            return nameTextView;
        }

	//--------------------------------------------------------------------------
	//  A getter method for the 'categorySpinner' variable.
	//--------------------------------------------------------------------------
        public Spinner getCategorySpinner() {
            return categorySpinner;
        }
    }

    //--------------------------------------------------------------------------
    //  A constructor for the Adapter.
    //--------------------------------------------------------------------------
    public ImportRecyclerAdapter(AllImportableContacts contacts, AllCategories categories) {
        this.contacts = contacts;
        this.categories = categories;
    }

    //--------------------------------------------------------------------------
    //  This method is responsible for inflating the layout.
    //--------------------------------------------------------------------------
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.import_list_item, parent, false);

        return new ViewHolder(view);
    }

    //--------------------------------------------------------------------------
    //  This method is responsible for binding data to the 'ViewHolder'.
    //--------------------------------------------------------------------------
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getNameTextView().setText(contacts.getNameAtIndex(position));
        holder.getNameTextView().setOnClickListener(v -> holder.getCheckBox().performClick());
        holder.itemView.setTag(R.id.tag_contact_id, contacts.getIdAtIndex(position));
        holder.itemView.setTag(R.id.tag_phone_numbers, contacts.getPhoneNumbersAtIndex(position));

	// Set up the 'Category' selection 'Spinner'.
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                holder.categorySpinner.getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                categories.getNamesArray()
                );
        holder.getCategorySpinner().setAdapter(spinnerAdapter);

	// Only display the 'Spinner' if the 'CheckBox' is selected.
        holder.getCheckBox().setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                holder.getCategorySpinner().setVisibility(View.VISIBLE);
            else
                holder.getCategorySpinner().setVisibility(View.GONE);
        });
    }

    //--------------------------------------------------------------------------
    //  This method returns the number of items in the 'RecyclerView'.
    //--------------------------------------------------------------------------
    @Override
    public int getItemCount() {
        return contacts.size();
    }
}

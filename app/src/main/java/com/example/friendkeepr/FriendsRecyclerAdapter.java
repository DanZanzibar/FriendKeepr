//******************************************************************************
//  FriendsRecyclerAdapter.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This class is a custom 'RecyclerAdapter' used to display an 'AllFriends'
//  object as a list with buttons for editing and deleting.
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

public class FriendsRecyclerAdapter extends RecyclerView.Adapter<FriendsRecyclerAdapter.ViewHolder> {

    private final FriendsByCategoryFragment parentFragment;
    private final AllFriends filteredFriends;

    //--------------------------------------------------------------------------
    //  This nested class is a custom 'ViewHolder' for displaying a 'Friend'
    //  object.
    //--------------------------------------------------------------------------
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;
        private final ImageButton deleteButtonView;
        private final ImageButton editButtonView;

	//--------------------------------------------------------------------------
	//  A constructor for the nested class that sets up references to various
	//  child Views.
	//--------------------------------------------------------------------------
        public ViewHolder(View view) {
            super(view);

            nameTextView = view.findViewById(R.id.friend_name_textview);
            deleteButtonView = view.findViewById(R.id.delete_button_view);
            editButtonView = view.findViewById(R.id.edit_button_view);
        }

	//--------------------------------------------------------------------------
	//  A getter method for the 'nameTextView' variable.
	//--------------------------------------------------------------------------
        public TextView getNameTextView() {
            return nameTextView;
        }

	//--------------------------------------------------------------------------
	//  A getter method for the 'deleteButtonView' variable.
	//--------------------------------------------------------------------------
        public ImageButton getDeleteButtonView() {
            return deleteButtonView;
        }

	//--------------------------------------------------------------------------
	//  A getter method for the 'editButtonView' variable.
	//--------------------------------------------------------------------------
        public ImageButton getEditButtonView() {
            return editButtonView;
        }
    }

    //--------------------------------------------------------------------------
    //  A constructor for the Adapter.
    //--------------------------------------------------------------------------
    public FriendsRecyclerAdapter(FriendsByCategoryFragment parentFragment,
                                  AllFriends filteredFriends) {
        this.parentFragment = parentFragment;
        this.filteredFriends = filteredFriends;
    }

    //--------------------------------------------------------------------------
    //  This method is responsible for inflating the layout for the 'ViewHolder'
    //  object.
    //--------------------------------------------------------------------------
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_list_item, parent, false);

        return new ViewHolder(view);
    }

    //--------------------------------------------------------------------------
    //  This method is responsible for binding data to the various child Views
    //  of the 'ViewHolder'.
    //--------------------------------------------------------------------------
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getNameTextView().setText(filteredFriends.getNameAtIndex(position));
        holder.itemView.setTag(R.id.tag_contact_id, filteredFriends.getIdAtIndex(position));

        String categoryName = filteredFriends.getCategoryNameAtIndex(position);

	// Sets up the buttons' actions.
        holder.getDeleteButtonView().setOnClickListener(
                v -> clickDeleteButton(position));
        holder.getEditButtonView().setOnClickListener(
                v -> clickEditButton(position, categoryName));
    }

    //--------------------------------------------------------------------------
    //  This method returns the number of items in the RecyclerView.
    //--------------------------------------------------------------------------
    @Override
    public int getItemCount() {
        return filteredFriends.size();
    }

    //--------------------------------------------------------------------------
    //  This helper method is invoked when the delete button is clicked and
    //  instantiates a 'FriendDeleteDialogFragment'. It passes the index of the
    //  associated 'Friend' object via a 'Bundle'.
    //--------------------------------------------------------------------------
    private void clickDeleteButton(int position) {
        Bundle args = new Bundle();
        args.putInt(FriendDeleteDialogFragment.INDEX_ARG, position);

        FriendDeleteDialogFragment dialogFragment = new FriendDeleteDialogFragment();
        dialogFragment.setArguments(args);
        dialogFragment.show(parentFragment.getChildFragmentManager(), "friend_delete");
    }

    //--------------------------------------------------------------------------
    //  This helper method is invoked when the edit button is clicked and
    //  instantiates a 'FriendDetailDialogFragment'. It passes the index of the
    //  associated 'Friend' object via a 'Bundle'.
    //--------------------------------------------------------------------------
    private void clickEditButton(int position, String categoryName) {
        Bundle args = new Bundle();
        args.putInt(FriendDetailDialogFragment.INDEX_ARG, position);
        args.putString(FriendDetailDialogFragment.CATEGORY_ARG, categoryName);

        FriendDetailDialogFragment dialogFragment = new FriendDetailDialogFragment();
        dialogFragment.setArguments(args);
        dialogFragment.show(parentFragment.getChildFragmentManager(), "friend_edit");
    }
}

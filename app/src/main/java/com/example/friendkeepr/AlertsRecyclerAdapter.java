//******************************************************************************
//  AlertsRecyclerAdapter.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This class is a custom 'RecyclerAdapter' for displaying 'AllAlerts'.
//******************************************************************************
package com.example.friendkeepr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlertsRecyclerAdapter extends RecyclerView.Adapter<AlertsRecyclerAdapter.ViewHolder> {

    private final AlertsFragment parentFragment;
    private final AllAlerts allAlerts;
    private final AllCategories allCategories;

    //--------------------------------------------------------------------------
    //  This nested class is a custom 'ViewHolder' representing each 'Alert'.
    //--------------------------------------------------------------------------
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView friendNameTextView;
        private final TextView daysToReminderTextView;
        private final TextView alertTextView;
        private final ImageButton callButton;
        private final ImageButton textButton;

	//--------------------------------------------------------------------------
	//  A constructor for the nested class that gets references for the various
	//  child Views.
	//--------------------------------------------------------------------------
        public ViewHolder(View view) {
            super(view);

            friendNameTextView = view.findViewById(R.id.friend_name_textview);
            daysToReminderTextView = view.findViewById(R.id.days_to_reminder_textview);
            alertTextView = view.findViewById(R.id.alert_textview);
            callButton = view.findViewById(R.id.call_button_view);
            textButton = view.findViewById(R.id.send_text_button_view);
        }

	//--------------------------------------------------------------------------
	//  A getter method for the nested class's 'friendNameTextView' variable.
	//--------------------------------------------------------------------------
        public TextView getFriendNameTextView() {
            return friendNameTextView;
        }

	//--------------------------------------------------------------------------
	//  A getter method for the nested class's 'daysToReminderTextView'
	//  variable.
	//--------------------------------------------------------------------------
        public TextView getDaysToReminderTextView() {
            return daysToReminderTextView;
        }

	//--------------------------------------------------------------------------
	//  A getter method for the nested class's 'alertTextView' variable.
	//--------------------------------------------------------------------------
        public TextView getAlertTextView() {
            return alertTextView;
        }

	//--------------------------------------------------------------------------
	//  A getter method for the nested class's 'callButton' variable.
	//--------------------------------------------------------------------------
        public ImageButton getCallButton() {
            return callButton;
        }

	//--------------------------------------------------------------------------
	//  A getter method for the nested class's 'textButton' variable.
	//--------------------------------------------------------------------------
        public ImageButton getTextButton() {
            return textButton;
        }
    }

    //--------------------------------------------------------------------------
    //  A constructor for the Adapter.
    //--------------------------------------------------------------------------
    public AlertsRecyclerAdapter(AlertsFragment parentFragment, AllAlerts allAlerts,
                                 AllCategories allCategories) {
        this.parentFragment = parentFragment;
        this.allAlerts = allAlerts;
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
                .inflate(R.layout.alert_list_item, parent, false);

        return new ViewHolder(view);
    }
    //--------------------------------------------------------------------------
    //  This method is responsible for binding data to the various child Views
    //  of the 'ViewHolder'.
    //--------------------------------------------------------------------------
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Alert alert = allAlerts.getAlertAtIndex(position);
        Friend friend = alert.getFriend();

        holder.getFriendNameTextView().setText(friend.getName());

	// If the value stored in the 'Friend' is '-1', get the value from the
	// 'Category' object.
        int daysToReminder = friend.getDaysToReminder();
        if (daysToReminder == -1)
            daysToReminder = allCategories.getDaysToReminderForName(friend.getCategory());

        String daysText = "Days to reminder: " + daysToReminder;
        holder.getDaysToReminderTextView().setText(daysText);

        String alertText = "Overdue: " + alert.periodToString();
        holder.getAlertTextView().setText(alertText);

        String phoneNumber = friend.getPhoneNumbers()[0];

	// Set up the call and text buttons.
        holder.getCallButton().setOnClickListener(v -> parentFragment.startPhoneCall(phoneNumber));
        holder.getTextButton().setOnClickListener(v -> parentFragment.startTextMessage(phoneNumber));
    }

    //--------------------------------------------------------------------------
    //  This method returns the number of items in the 'RecyclerView'.
    //--------------------------------------------------------------------------
    @Override
    public int getItemCount() {
        if (allAlerts != null)
            return allAlerts.size();
        else
            return 0;
    }
}

//******************************************************************************
//  AlertsFragment.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This fragment is the main screen for displaying Alerts to the user.
//******************************************************************************
package com.example.friendkeepr;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.Telephony;
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class AlertsFragment extends FriendKeeprFragment {

    private AlertsViewModel viewModel;
    private RecyclerView recyclerView;
    private TextView loadingTextView;
    private TextView noAlertsTextView;

    //--------------------------------------------------------------------------
    //  This method is responsible for inflating the correct layout.
    //--------------------------------------------------------------------------
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alerts, container, false);
    }

    //--------------------------------------------------------------------------
    //  This method sets up the ViewModel and all Observers.
    //--------------------------------------------------------------------------
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(AlertsViewModel.class);

        recyclerView = view.findViewById(R.id.alerts_recycler_view);
        loadingTextView = view.findViewById(R.id.loading_text_view);
        noAlertsTextView = view.findViewById(R.id.no_alerts_textview);

        Observer<Boolean> loadingObserver = this::displayLoadingScreen;
        viewModel.getIsLoadingLiveData().observe(getViewLifecycleOwner(), loadingObserver);

        Observer<AllAlerts> alertsObserver = this::generateAlertsList;
        viewModel.getAllAlertsLiveData().observe(getViewLifecycleOwner(), alertsObserver);

        Observer<AllCategories> categoriesObserver = this::generateAlertsList;
        viewModel.getAllCategoriesLiveData().observe(getViewLifecycleOwner(), categoriesObserver);
    }

    //--------------------------------------------------------------------------
    //  This method updated the data in the ViewModel asynchronously.
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
    //  This method fetches data from storage and posts the values to the
    //  ViewModel. Should be called in a background thread.
    //--------------------------------------------------------------------------
    public void updateViewModel() {
        AllCategories allCategories = getAppData().getCategories();
        viewModel.postAllCategories(allCategories);
        AllAlerts allAlerts = new AllAlerts(generateAlertArray());
        viewModel.postAllAlerts(allAlerts);
        viewModel.postIsLoading(false);
    }

    //--------------------------------------------------------------------------
    //  This method starts a phone call to the specified phone number.
    //--------------------------------------------------------------------------
    public void startPhoneCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    //--------------------------------------------------------------------------
    //  This method starts a text message to the specified phone number.
    //--------------------------------------------------------------------------
    public void startTextMessage(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + phoneNumber));
        startActivity(intent);
    }

    //--------------------------------------------------------------------------
    //  This method is designed to be an Observer for the 'AllAlerts' object
    //  stored in the ViewModel. It sets up 'recyclerView' to display all the
    //  Alerts.
    //--------------------------------------------------------------------------
    private void generateAlertsList(AllAlerts allAlerts) {
        noAlertsTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(new AlertsRecyclerAdapter(this, allAlerts,
                viewModel.getAllCategories()));

	// If the 'allAlerts' object is empty, display the no alerts message to
	// the user.
        if (allAlerts != null && allAlerts.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            noAlertsTextView.setVisibility(View.VISIBLE);
        }
    }

    //--------------------------------------------------------------------------
    //  This is an overloaded version of the previous method designed to be an
    //  Observer of the 'AllCategories' object stored in the ViewModel.
    //--------------------------------------------------------------------------
    private void generateAlertsList(AllCategories allCategories) {
        noAlertsTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        AllAlerts allAlerts = viewModel.getAllAlerts();

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(new AlertsRecyclerAdapter(this, allAlerts,
                allCategories));

	// If the 'allAlerts' object is empty, display the no alerts message to
	// the user.
        if (allAlerts != null && allAlerts.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            noAlertsTextView.setVisibility(View.VISIBLE);
        }
    }

    //--------------------------------------------------------------------------
    //  This method returns an array of 'Alert' for creating the 'AllAlerts'
    //  object.
    //--------------------------------------------------------------------------
    private Alert[] generateAlertArray() {
        AllFriends allFriends = getAppData().getAllFriends();
        AllCategories allCategories = getAppData().getCategories();

        List<Alert> alertList = new ArrayList<>();

	// Iterate over 'allFriends' to see if each 'Friend' generates a
	// non-null 'Alert'. Add to the array if so.
        for (Friend friend : allFriends) {
            Alert alert = generateAlert(friend, allCategories);
            if (alert != null)
                alertList.add(alert);
        }

        return alertList.toArray(new Alert[0]);
    }

    //--------------------------------------------------------------------------
    //  This method returns an 'Alert' for a given 'Friend' if they are past due
    //  a contact. It will return 'null' if not.
    //--------------------------------------------------------------------------
    private Alert generateAlert(Friend friend, AllCategories allCategories) {
        int daysToReminder = friend.getDaysToReminder();

	// If 'friends.getDaysToReminder()' is '-1', this means to check the
	// category data for the proper value.
        if (daysToReminder == -1)
            daysToReminder = allCategories.getDaysToReminderForName(friend.getCategory());

        LocalDate currentDate = LocalDate.now();
        LocalDate dateOfReminder = getDateOfLastCallOrText(friend).plusDays(daysToReminder);

        Alert alert = null;

	// If past due, create the 'Alert'.
        if (!currentDate.isBefore(dateOfReminder))
            alert = new Alert(friend, dateOfReminder.until(currentDate));

        return alert;
    }

    //--------------------------------------------------------------------------
    //  This method returns a 'LocalDate' object representing the last recorded
    //  call (of more than Alert.MIN_CALL_SECONDS) or the last recorded outgoing
    //  text, whichever is more recent.
    //--------------------------------------------------------------------------
    private LocalDate getDateOfLastCallOrText(Friend friend) {
        String[] phoneNumbers = friend.getPhoneNumbers();
        LocalDate mostRecentDate = LocalDate.now().minusYears(11);

	// Iterate over a Friend's phone numbers to check for all calls/texts,
	// always recording the most recent item found.
        for (String phoneNumber : phoneNumbers) {
            LocalDate dateOfLastCall = getDateOfLastCall(phoneNumber);
            if (dateOfLastCall.isAfter(mostRecentDate))
                mostRecentDate = dateOfLastCall;

            LocalDate dateOfLastText = getDateOfLastText(phoneNumber);
            if (dateOfLastText.isAfter(mostRecentDate))
                mostRecentDate = dateOfLastText;
        }

        return mostRecentDate;
    }

    //--------------------------------------------------------------------------
    //  This method returns a 'LocalDate' object representing the date of the
    //  last recorded call of more than Alert.MIN_CALL_SECONDS.
    //--------------------------------------------------------------------------
    private LocalDate getDateOfLastCall(String phoneNumber) {

	// Build the 'ContentResolver' query.
        Uri uri = CallLog.Calls.CONTENT_URI;
        String[] projection = {
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION
        };
        String selection = CallLog.Calls.CACHED_NORMALIZED_NUMBER + " = ? AND " +
                CallLog.Calls.DURATION + " > ?";
        String[] selectionArgs = {phoneNumber, Alert.MIN_CALL_SECONDS};
        String sortOrder = CallLog.Calls.DATE + " DESC";

        Cursor cursor = requireContext().getContentResolver().query(
                uri, projection, selection, selectionArgs, sortOrder);

        long mostRecentDateEpoch = 0;

	// If the 'cursor' comes back non-null and non-empty, retrieve the date
	// in Epoch time and close the 'cursor' (even if empty). 
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int mostRecentIndex = cursor.getColumnIndexOrThrow(CallLog.Calls.DATE);
                mostRecentDateEpoch = cursor.getLong(mostRecentIndex);
            }
            cursor.close();
        }

        return getDateOfEpochTime(mostRecentDateEpoch);
    }

    //--------------------------------------------------------------------------
    //  This method returns a 'LocalDate' object representing the date of the
    //  last outgoing SMS message to a given phone number. 
    //--------------------------------------------------------------------------
    private LocalDate getDateOfLastText(String phoneNumber) {

	// Build the 'ContentResolver' query.
        Uri uri = Telephony.Sms.Sent.CONTENT_URI;
        String[] projection = {Telephony.Sms.Sent.DATE};
        String selection = Telephony.Sms.Sent.ADDRESS + " = ?";
        String[] selectionArgs = {phoneNumber};
        String sortOrder = Telephony.Sms.Sent.DATE + " DESC";

        Cursor cursor = requireContext().getContentResolver().query(
                uri, projection, selection, selectionArgs, sortOrder);

        long mostRecentDateEpoch = 0;

	// If the 'cursor' comes back non-null and non-empty, retrieve the date
	// in Epoch time and close the 'cursor' (even if empty).
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int mostRecentIndex = cursor.getColumnIndexOrThrow(Telephony.Sms.Sent.DATE);
                mostRecentDateEpoch = cursor.getLong(mostRecentIndex);
            }
            cursor.close();
        }

        return getDateOfEpochTime(mostRecentDateEpoch);
    }

    //--------------------------------------------------------------------------
    //  This method takes a value for Epoch time and returns a 'LocalDate'
    //  object.
    //--------------------------------------------------------------------------
    private LocalDate getDateOfEpochTime(long epochTime) {
        LocalDateTime dateTime = Instant.ofEpochMilli(epochTime).atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return dateTime.toLocalDate();
    }

    //--------------------------------------------------------------------------
    //  This method is designed to be an Observer for the 'isLoading' variable
    //  contained in the ViewModel. It shows a loading screen when data is being
    //  fetched.
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

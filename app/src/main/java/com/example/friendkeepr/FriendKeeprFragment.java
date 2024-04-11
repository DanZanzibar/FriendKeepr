//******************************************************************************
//  FriendKeeprFragment.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This abstract class is extended for all Fragment classes in the app. It
//  provides a convenience method for accessing the 'FriendKeeprData' object
//  contained in the 'MainActivity'. 
//******************************************************************************
package com.example.friendkeepr;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

import androidx.fragment.app.Fragment;

public abstract class FriendKeeprFragment extends Fragment {

    //--------------------------------------------------------------------------
    //  This method returns the 'FriendKeeprData' object in the 'MainActivity'.
    //--------------------------------------------------------------------------
    public FriendKeeprData getAppData() {
        MainActivity mainActivity = (MainActivity) requireActivity();

        return mainActivity.getAppData();
    }

    //--------------------------------------------------------------------------
    //  This method is used by all Fragment to fade in the main content once
    //  done loading.
    //--------------------------------------------------------------------------
    public void fadeInContent(View loadingView, View... viewsEntering) {
        int animationDuration = getResources().getInteger(android.R.integer.config_mediumAnimTime);

	// Iterate over all 'viewsEntering' and set up fade in.
        for (View view : viewsEntering) {
            view.setAlpha(0f);
            view.setVisibility(View.VISIBLE);
            view.animate()
                    .alpha(1f)
                    .setDuration(animationDuration)
                    .setListener(null);
        }

        loadingView.animate()
                .alpha(0f)
                .setDuration(animationDuration)

	    // If the animation is canceled of finished, set visibilities.
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loadingView.setVisibility(View.GONE);
                        for (View view : viewsEntering)
                            view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        loadingView.setVisibility(View.GONE);
                        for (View view : viewsEntering)
                            view.setVisibility(View.VISIBLE);
                    }
                });
    }
}

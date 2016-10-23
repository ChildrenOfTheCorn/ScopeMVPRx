package my.beelzik.mobile.scopemvptest.mvp.util;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andrey on 23.10.2016.
 */

public class ViewStateHelper {


    private static final String STATE_KEY_FOCUSED = "STATE_KEY_FOCUSED";
    private static final String STATE_KEY_VISIBILITY = "STATE_KEY_VISIBILITY";
    private static final String STATE_KEY_ENABLED = "STATE_KEY_ENABLED";


    private List<View> mViewList;

    private String mTag;

    public ViewStateHelper(Object containerObj) {
        this(containerObj.getClass().getSimpleName());
    }

    public ViewStateHelper(String tag) {
        mViewList = new ArrayList<>();
        mTag = tag;
    }


    public void addView(View view) {
        mViewList.add(view);
    }

    public void addViews(View... views) {
        Collections.addAll(mViewList, views);
    }

    public void onSaveInstanceState(Bundle outSate) {

        for (View view : mViewList) {
            String keyEnabled = mTag + "_" + STATE_KEY_ENABLED + "_" + view.getId();
            String keyFocused = mTag + "_" + STATE_KEY_FOCUSED + "_" + view.getId();
            String keyVisibility = mTag + "_" + STATE_KEY_VISIBILITY + "_" + view.getId();
            outSate.putBoolean(keyEnabled, view.isEnabled());
            outSate.putInt(keyVisibility, view.getVisibility());
            outSate.putBoolean(keyFocused, view.isFocusable());
        }
    }

    public void onRestoreInstanceState(Bundle state) {

        if (state != null) {
            for (View view : mViewList) {
                String keyEnabled = mTag + "_" + STATE_KEY_ENABLED + "_" + view.getId();
                String keyFocused = mTag + "_" + STATE_KEY_FOCUSED + "_" + view.getId();
                String keyVisibility = mTag + "_" + STATE_KEY_VISIBILITY + "_" + view.getId();
                view.setEnabled(state.getBoolean(keyEnabled));

                boolean isFocused = state.getBoolean(keyFocused);

                if (isFocused) {
                    view.requestFocus();
                }

                view.setVisibility(state.getInt(keyVisibility));

            }
        }


    }

}

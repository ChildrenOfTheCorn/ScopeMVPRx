package my.beelzik.mobile.scopemvptest.utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Andrey on 16.10.2016.
 */

public class ViewUtils {


    public static void enableHierarchy(ViewGroup root, boolean enable) {

        root.setEnabled(enable);
        for (int i = 0; i < root.getChildCount(); i++) {
            View childView = root.getChildAt(i);

            if (childView instanceof ViewGroup) {
                ViewGroup childGroup = (ViewGroup) childView;
                enableHierarchy(childGroup, enable);
            } else {
                childView.setEnabled(enable);
            }
        }
    }
}

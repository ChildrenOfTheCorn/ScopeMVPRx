package my.beelzik.mobile.scopemvptest.ui.view;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import my.beelzik.mobile.scopemvptest.App;
import my.beelzik.mobile.scopemvptest.R;


public class ItemDecorationWithDivider extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int mPaddingLeft;
    private int mPaddingRight;


    public ItemDecorationWithDivider() {
        this(0, 0);
    }


    public ItemDecorationWithDivider(int paddingLeft, int paddingRight) {
        mDivider = ContextCompat.getDrawable(App.getContext(), R.drawable.drawable_shape_recycler_divider);
        mPaddingLeft = paddingLeft;
        mPaddingRight = paddingRight;
    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft() + mPaddingLeft;
        int right = parent.getWidth() - (parent.getPaddingRight() + mPaddingRight);

        int childCount = getChildCount(parent);
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();


            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }


    protected int getChildCount(RecyclerView parent) {
        return parent.getChildCount();
    }
}

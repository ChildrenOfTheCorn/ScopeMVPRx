package my.beelzik.mobile.scopemvptest.ui.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import my.beelzik.mobile.scopemvptest.R;
import my.beelzik.mobile.scopemvptest.ui.adapter.BaseListRecyclerAdapter;
import my.beelzik.mobile.scopemvptest.ui.view.ItemDecorationWithDivider;

/**
 * Created by Andrey on 23.10.2016.
 */

public class DefaultRecyclerLoadMoreListHolder<T> extends AbsRecyclerLoadMoreListHolder<T> {


    public DefaultRecyclerLoadMoreListHolder(View view, BaseListRecyclerAdapter<T> adapter) {
        super(view, adapter);

        mRecyclerView.addItemDecoration(new ItemDecorationWithDivider() {
            @Override
            protected int getChildCount(RecyclerView parent) {
                return mWrapperAdapter.isInProgress() ? super.getChildCount(parent) - 1 : super.getChildCount(parent);
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


    @Override
    protected int getProgressLayoutRes() {
        return R.layout.cell_load_more_progress;
    }

    @Override
    public void showLoadError(CharSequence message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}

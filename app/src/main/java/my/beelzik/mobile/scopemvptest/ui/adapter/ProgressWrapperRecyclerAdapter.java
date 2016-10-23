package my.beelzik.mobile.scopemvptest.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Created by Andrey on 23.10.2016.
 */
@Accessors(prefix = "m")
public class ProgressWrapperRecyclerAdapter extends RecyclerView.Adapter {

    public static final int VIEW_TYPE_PROGRESS = -4000;

    private Context mContext;
    private RecyclerView.Adapter mRootAdapter;

    @LayoutRes
    private int mProgressLayoutRes;

    @Getter private boolean mInProgress = false;


    public ProgressWrapperRecyclerAdapter(Context context, RecyclerView.Adapter rootAdapter, @LayoutRes int progressLayoutRes) {
        mContext = context;
        mRootAdapter = rootAdapter;
        mProgressLayoutRes = progressLayoutRes;
    }

    @Override
    public int getItemViewType(int position) {

        if (!mInProgress || position != (getItemCount() - 1)) {
            return mRootAdapter.getItemViewType(position);
        }
        return VIEW_TYPE_PROGRESS;
    }

    public void setInProgress(boolean inProgress) {
        mInProgress = inProgress;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_PROGRESS) {
            View progressView = LayoutInflater.from(mContext).inflate(mProgressLayoutRes, parent, false);
            return new ProgressHolder(progressView);
        } else {
            return mRootAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!mInProgress || position != (getItemCount() - 1)) {
            mRootAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mInProgress ? mRootAdapter.getItemCount() + 1 : mRootAdapter.getItemCount();
    }

    protected class ProgressHolder extends RecyclerView.ViewHolder {

        protected ProgressHolder(View itemView) {
            super(itemView);
        }
    }
}

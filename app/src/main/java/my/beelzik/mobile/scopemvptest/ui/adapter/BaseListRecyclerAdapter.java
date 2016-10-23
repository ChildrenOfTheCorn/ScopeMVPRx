package my.beelzik.mobile.scopemvptest.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import lombok.Setter;

/**
 * Created by Andrey on 17.10.2016.
 */

public abstract class BaseListRecyclerAdapter<T> extends RecyclerView.Adapter<BaseListRecyclerAdapter.BaseItemHolder<T>> {

    protected List<T> sourceList;

    @Setter protected OnItemClickListener<T> mItemClickListener;

    protected final Context context;

    protected final LayoutInflater inflater;


    public BaseListRecyclerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<T> sourceList) {
        this.sourceList = sourceList;
        notifyDataSetChanged();
    }

    @Override
    public BaseItemHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseItemHolder<T> holder = this.onCreateViewHolder(inflater, parent, viewType);
        holder.itemView.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(holder, holder.mItem, holder.getAdapterPosition());
            }
        });
        return holder;
    }


    public abstract BaseItemHolder<T> onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(BaseItemHolder<T> holder, int position) {
        holder.bind(sourceList.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (sourceList != null) {
            return sourceList.size();
        }
        return 0;
    }

    public abstract static class BaseItemHolder<I> extends RecyclerView.ViewHolder {

        private I mItem;

        public BaseItemHolder(LayoutInflater inflater, ViewGroup parent, @LayoutRes int cellLayoutRes) {
            this(inflater.inflate(cellLayoutRes, parent, false));
        }


        public BaseItemHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind(I item, int position) {
            mItem = item;
            onBind(item, position);

        }

        protected abstract void onBind(I item, int position);
    }

    public interface OnItemClickListener<T> {

        void onItemClick(BaseItemHolder<T> holder, T item, int position);
    }
}

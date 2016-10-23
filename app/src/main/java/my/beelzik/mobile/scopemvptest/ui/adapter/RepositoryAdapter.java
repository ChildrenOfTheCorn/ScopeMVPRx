package my.beelzik.mobile.scopemvptest.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import my.beelzik.mobile.scopemvptest.R;
import my.beelzik.mobile.scopemvptest.model.Repository;

/**
 * Created by Andrey on 23.10.2016.
 */

public class RepositoryAdapter extends BaseListRecyclerAdapter<Repository> {


    public RepositoryAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseItemHolder<Repository> onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new RepositoryHolder(inflater, parent, R.layout.cell_repository);
    }


    protected class RepositoryHolder extends BaseItemHolder<Repository> {

        @BindView(R.id.name)
        public TextView name;

        @BindView(R.id.image)
        public ImageView image;

        public RepositoryHolder(LayoutInflater inflater, ViewGroup parent, @LayoutRes int cellLayoutRes) {
            super(inflater, parent, cellLayoutRes);
        }

        @Override
        protected void onBind(Repository item, int position) {
            name.setText("Position: " + position + " name: " + item.getName());

            if (item.getOwner() != null) {
                Glide.with(context)
                        .load(item.getOwner().getAvatarUrl())
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .override(200, 200)
                        .crossFade()
                        .into(image);
            } else {
                Log.d("TAG", "item.getOwner() == null: " + item);
            }


        }
    }
}

package com.udacity.sandwichclub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SandwichAdapter extends RecyclerView.Adapter<SandwichAdapter.ViewHolder> {

    private Context mContext;
    private final ListItemClickListener mOnClickListener;
    private final ListItemLongClickListener mOnLongClickListener;

    public interface ListItemClickListener {
        void onItemClickListener(int viewIndex);
    }

    public interface ListItemLongClickListener {
        void onItemClickListener(Sandwich sandwich);
    }

    private List<Sandwich> mSandwiches = new ArrayList<Sandwich>() { //lista originala
    };

    SandwichAdapter(Context context, ListItemClickListener listener, ListItemLongClickListener longClick) {
        mOnLongClickListener = longClick;
        mOnClickListener = listener;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.app_adapter_gridview_item, parent, false);
        return new ViewHolder(view);
    }

    void setSandwiches(List<Sandwich> sandwiches) {
        mSandwiches = sandwiches;
        notifyDataSetChanged();
    }

    public List<Sandwich> getSandwiches() {
        return mSandwiches;
    }

    @Override
    public int getItemCount() {
        return mSandwiches != null ? mSandwiches.size() : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Sandwich currentSandwich = mSandwiches.get(position);

        holder.sandwichName.setText(currentSandwich.getMainName());
        holder.sandwichName.setTextColor(Color.parseColor(Constants.SANDWICH_NAME_COLOR));

        if (currentSandwich.getImage().contains(mContext.getString(R.string.http)))
            Picasso.get().load(currentSandwich.getImage()).into(holder.sandwichIcon);
        else
            Picasso.get().load(mContext.getString(R.string.file_start_path) + currentSandwich.getImage()).config(Bitmap.Config.RGB_565).fit().centerCrop().into(holder.sandwichIcon);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.sandwiches_icon)
        ImageView sandwichIcon = itemView.findViewById(R.id.sandwiches_icon);
        @BindView(R.id.sandwich_name)
        TextView sandwichName = itemView.findViewById(R.id.sandwich_name);

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int viewIndex = getAdapterPosition();
            mOnClickListener.onItemClickListener(mSandwiches.get(viewIndex).getId());
        }

        @Override
        public boolean onLongClick(View view) {
            int viewIndex = getAdapterPosition();
            mOnLongClickListener.onItemClickListener(mSandwiches.get(viewIndex));
            return true;
        }
    }
}

package com.example.monikasaini.preview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.monikasaini.preview.R;
import com.example.monikasaini.preview.models.PageData;

import java.util.List;

/**
 * Created by Monika on 06/06/17.
 */

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.ViewHolder> {

    private List<PageData> dataList;
    private Context context;

    public PreviewAdapter(Context context, List<PageData> items) {
        dataList = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_preview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final PageData data = dataList.get(position);
        if (data != null) {
            if (data.getTitle() != null)
                holder.title.setText(data.getTitle());
            if (data.getDescription() != null)
                holder.description.setText(data.getDescription());
            if (data.getDisplayImage() != null)
                Glide.with(context)
                        .load(data.getDisplayImage())
                        .into(holder.imageView);
            else if (data.getImages() != null && data.getImages().size() > 0)
                Glide.with(context)
                        .load(data.getImages().get(0))
                        .into(holder.imageView);
        }


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setDataList(List<PageData> dataList) {
        this.dataList = dataList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView title;
        public final ImageView imageView;
        public final TextView description;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            title = (TextView) view.findViewById(R.id.title);
            imageView = (ImageView) view.findViewById(R.id.image_view);
            description = (TextView) view.findViewById(R.id.description);

        }
    }
}



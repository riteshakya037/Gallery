package com.blues.gallery.Adaptors;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.blues.gallery.Helper.AppConstant;
import com.blues.gallery.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suleiman19 on 10/22/15.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyItemHolder> {

    Context context;
    ArrayList<ImageModel> data = new ArrayList<>();

    public GalleryAdapter(Context context, ArrayList<ImageModel> data) {
        this.context = context;
        this.data = data;
    }


    @Override
    public MyItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyItemHolder viewHolder;
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.image_list, parent, false);
        viewHolder = new MyItemHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyItemHolder holder, int position) {
        Glide.with(context).load(data.get(position).getUrl())
                .thumbnail(0.5f)
                .override(200, 200)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.mImg);
        if (data.get(position).getName().equals(AppConstant.overlayCheckText)) {
            System.out.println("data = " + data.get(position).getName());
            holder.overlay.setVisibility(View.VISIBLE);
        } else {
            holder.overlay.setVisibility(View.GONE);
        }
        holder.imageHolder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData(ArrayList<ImageModel> newData) {
        this.data = newData;
    }

    public ArrayList<ImageModel> getData() {
        return data;
    }

    public static class MyItemHolder extends RecyclerView.ViewHolder {
        FrameLayout imageHolder;
        ImageView mImg;
        ImageView overlay;

        public MyItemHolder(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.item_img);
            overlay = (ImageView) itemView.findViewById(R.id.item_overlay);
            imageHolder = (FrameLayout) itemView.findViewById(R.id.imageHolder);
        }

    }


}

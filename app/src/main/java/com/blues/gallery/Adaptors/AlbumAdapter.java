package com.blues.gallery.Adaptors;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blues.gallery.Helper.Utils;
import com.blues.gallery.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Suleiman19 on 10/22/15.
 */
public class AlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<String> albumList;
    Context context;
    HashMap<String, ArrayList<ImageModel>> data = new HashMap<>();

    public AlbumAdapter(Context context, HashMap<String, ArrayList<ImageModel>> data) {
        this.context = context;
        this.data = data;
        this.albumList = new ArrayList<>(data.keySet());
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.album_view, parent, false);
        viewHolder = new MyItemHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ArrayList<ImageModel> imageModels = data.get(albumList.get(position));
        Glide.with(context).load(imageModels.get(0).getUrl())
                .thumbnail(0.5f)
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(((MyItemHolder) holder).imageViewLarge);
        Glide.with(context).load(imageModels.get(0).getUrl())
                .thumbnail(0.5f)
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(((MyItemHolder) holder).imageViewTop);
        if (imageModels.size() > 1)
            Glide.with(context).load(imageModels.get(1).getUrl())
                    .thumbnail(0.5f)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(((MyItemHolder) holder).imageViewBottom);
        ((MyItemHolder) holder).albumName.setText(Utils.getName(albumList.get(position)));
        ((MyItemHolder) holder).albumSize.setText(String.format(Locale.US, "%d %s", imageModels.size(), context.getResources().getString(R.string.photos)));
        Utils utils = new Utils(context);
        int proportionalHeight;
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            proportionalHeight = (int) (utils.getScreenWidth() / 1.5);
        else
            proportionalHeight = (int) (utils.getScreenWidth() / 3);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, proportionalHeight); // (width, height)
        ((MyItemHolder) holder).container.setLayoutParams(params);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyItemHolder extends RecyclerView.ViewHolder {
        ImageView imageViewTop;
        ImageView imageViewBottom;
        ImageView imageViewLarge;
        TextView albumName;
        TextView albumSize;
        FrameLayout container;

        public MyItemHolder(View itemView) {
            super(itemView);

            imageViewLarge = (ImageView) itemView.findViewById(R.id.imageViewLarge);
            imageViewTop = (ImageView) itemView.findViewById(R.id.imageViewTop);
            imageViewBottom = (ImageView) itemView.findViewById(R.id.imageViewBottom);
            albumName = (TextView) itemView.findViewById(R.id.albumName);
            albumSize = (TextView) itemView.findViewById(R.id.albumSize);
            container = (FrameLayout) itemView.findViewById(R.id.container);
        }

    }


}

package com.blues.gallery.Adaptors;

import android.content.ClipData;
import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    private boolean overlayCheck;

    public GalleryAdapter(Context context, ArrayList<ImageModel> data, boolean overlayCheck) {
        this.context = context;
        this.data = data;
        this.overlayCheck = overlayCheck;
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
        if (data.get(position).getName().equals(AppConstant.overlayCheckText) && overlayCheck) {
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
//        final int longClickDuration = 300;
//        final boolean[] isLongPress = {false};
//
//        holder.imageHolder.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(final View view, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    isLongPress[0] = true;
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (isLongPress[0]) {
////                                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
////                                vibrator.vibrate(100);
//                                // set your code here
//                                ClipData data = ClipData.newPlainText("", "");
//                                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
//                                view.startDrag(data, shadowBuilder, view, 0);
//                            }
//                        }
//                    }, longClickDuration);
//                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    isLongPress[0] = false;
//                }
//                return true;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData(ArrayList<ImageModel> newData, boolean overlayCheck) {
        this.data = newData;
        this.overlayCheck = overlayCheck;
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

package com.blues.gallery.Adaptors;

import android.content.ClipData;
import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
    private Listener mListener;

    public GalleryAdapter(Context context, ArrayList<ImageModel> data,  Listener listener) {
        this.context = context;
        this.data = data;
        this.mListener = listener;
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
            holder.overlay.setVisibility(View.VISIBLE);
        } else {
            holder.overlay.setVisibility(View.GONE);
        }
        holder.imageHolder.setTag(position);
        holder.imageHolder.setContentDescription(data.get(position).getName());
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

    public DragListener getDragInstance() {
        if (mListener != null) {
            return new DragListener(mListener);
        } else {
            Log.e("Route Adapter: ", "Initialize listener first!");
            return null;
        }
    }

    public void clearData() {
        data.clear();
        this.notifyDataSetChanged();
    }

    public interface Listener {
        void setEmptyList(boolean visibility);
    }

    public ImageModel getItem(int position) {
        return data.get(position);
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
        CheckBox item_check;

        public MyItemHolder(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.item_img);
            overlay = (ImageView) itemView.findViewById(R.id.item_overlay);
            imageHolder = (FrameLayout) itemView.findViewById(R.id.imageHolder);
            item_check = (CheckBox) itemView.findViewById(R.id.item_check);
        }

    }

    public class DragListener implements View.OnDragListener {

        boolean isDropped = false;
        Listener mListener;

        public DragListener(Listener listener) {
            this.mListener = listener;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;

                case DragEvent.ACTION_DRAG_ENTERED:
                    //v.setBackgroundColor(Color.LTGRAY);
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    //v.setBackgroundColor(Color.YELLOW);
                    break;

                case DragEvent.ACTION_DROP:

                    isDropped = true;
                    int positionSource;

                    View viewSource = (View) event.getLocalState();
                    if ((v.getId() == R.id.containerRecycle && ((View) viewSource.getParent()).getId() == R.id.momentsView)
                            || v.getId() == R.id.gallerySmall
                            ) {
                        RecyclerView target;
                        if (v.getId() == R.id.gallerySmall) {
                            target = (RecyclerView)
                                    v.getRootView().findViewById(R.id.containerRecycle);
                        } else {
                            target = (RecyclerView) v;
                        }
                        RecyclerView source = (RecyclerView) viewSource.getParent();
                        GalleryAdapter adapterSource = (GalleryAdapter) source.getAdapter();
                        positionSource = (int) viewSource.getTag();
                        MyItemHolder momentImageView = (MyItemHolder) source.findViewHolderForAdapterPosition(positionSource);
//                        momentImageView.item_check.setVisibility(View.VISIBLE);

                        ImageModel customList = adapterSource.getData().get(positionSource);

                        GalleryAdapter adapterTarget = (GalleryAdapter) target.getAdapter();
                        ArrayList<ImageModel> customListTarget = adapterTarget.getData();
                        if (!customListTarget.contains(customList)) {
                            customListTarget.add(customList);
                            adapterTarget.updateData(customListTarget);
                            adapterTarget.notifyDataSetChanged();
                        }
                        if (adapterTarget.getItemCount() < 1) {
                            mListener.setEmptyList(true);
                        }

                        if (v.getId() == R.id.gallerySmall) {
                            mListener.setEmptyList(false);
                        }
                    } else if (((View) viewSource.getParent()).getId() == R.id.containerRecycle
                            && v.getId() == R.id.dumpLocation) {
                        RecyclerView source = (RecyclerView) viewSource.getParent();
                        GalleryAdapter adapterSource = (GalleryAdapter) source.getAdapter();
                        positionSource = (int) viewSource.getTag();

                        ArrayList<ImageModel> customListSource = adapterSource.getData();

                        ImageModel imageToRemove = customListSource.get(positionSource);
                        RecyclerView momentView = (RecyclerView) v.getRootView().findViewById(R.id.momentsView);
                        GalleryAdapter momentAdapter = (GalleryAdapter) momentView.getAdapter();
                        MyItemHolder momentImageView = (MyItemHolder) momentView.findViewHolderForAdapterPosition(momentAdapter.getData().indexOf(imageToRemove));

                        momentImageView.item_check.setVisibility(View.INVISIBLE);
                        customListSource.remove(positionSource);
                        adapterSource.updateData(customListSource);
                        adapterSource.notifyDataSetChanged();
                        if (adapterSource.getItemCount() < 1) {
                            mListener.setEmptyList(true);
                        }

                        if (v.getId() == R.id.gallerySmall) {
                            mListener.setEmptyList(false);
                        }
                    }

                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    //v.setBackgroundColor(0);
                    break;

                default:
                    break;
            }
//
//            if (!isDropped) {
//                View vw = (View) event.getLocalState();
//                vw.setVisibility(View.VISIBLE);
//            }

            return true;
        }

    }
}

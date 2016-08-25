package com.blues.gallery.Adaptors;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.blues.gallery.CustomViews.SquareFrameLayout;
import com.blues.gallery.Helper.Utils;
import com.blues.gallery.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

/**
 * Created by Suleiman19 on 10/22/15.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyItemHolder> {

    Context context;
    ArrayList<ImageModel> data = new ArrayList<>();
    private ArrayList<Integer> markedPos;
    private boolean smallerIcon = false;
    private Listener mListener;
    Utils utils;

    public GalleryAdapter(Context context, ArrayList<ImageModel> data, ArrayList<Integer> markedPos, Listener listener) {
        this(context, data, markedPos, false, listener);
    }

    public GalleryAdapter(Context context, ArrayList<ImageModel> data, ArrayList<Integer> markedPos, boolean smallerIcon, Listener listener) {
        this.context = context;
        this.data = data;
        this.markedPos = markedPos;
        this.smallerIcon = smallerIcon;
        this.mListener = listener;
        utils = new Utils(context);
    }


    @Override
    public MyItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyItemHolder viewHolder;
        if (smallerIcon) {
            View v;
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.image_list_small, parent, false);
            viewHolder = new MyItemHolder(v);
            return viewHolder;
        } else {
            View v;
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.image_list, parent, false);
            viewHolder = new MyItemHolder(v);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(final MyItemHolder holder, final int position) {
        Glide.with(context).load(data.get(position).getUrl())
                .thumbnail(0.5f)
                .override(200, 200)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (data.get(position).isCheckJpeg()) {
                            holder.overlay.setVisibility(View.VISIBLE);
                        } else {
                            holder.overlay.setVisibility(View.GONE);
                        }
                        return false;
                    }
                })
                .into(holder.mImg);


        if (markedPos.contains(position)) {
            holder.item_check.setVisibility(View.VISIBLE);
        } else {
            holder.item_check.setVisibility(View.GONE);
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

    public ArrayList<Integer> getMarkedPos() {
        return markedPos;
    }

    public interface Listener {
        void setEmptyList(boolean visibility);
    }


    public void updateData(ArrayList<ImageModel> newData, ArrayList<Integer> markedPos) {
        if (newData != null)
            this.data = newData;
        this.markedPos = markedPos;
    }

    public ArrayList<ImageModel> getData() {
        return data;
    }

    public static class MyItemHolder extends RecyclerView.ViewHolder {
        SquareFrameLayout imageHolder;
        ImageView mImg;
        ImageView overlay;
        CheckBox item_check;

        public MyItemHolder(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.item_img);
            overlay = (ImageView) itemView.findViewById(R.id.item_overlay);
            imageHolder = (SquareFrameLayout) itemView.findViewById(R.id.imageHolder);
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
            EditText editText = (EditText) v.getRootView().findViewById(R.id.collection_title);
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    editText.setCursorVisible(false);
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

                        ImageModel customList = adapterSource.getData().get(positionSource);
                        ArrayList<Integer> currentSourceList = adapterSource.getMarkedPos();

                        GalleryAdapter adapterTarget = (GalleryAdapter) target.getAdapter();
                        ArrayList<ImageModel> customListTarget = adapterTarget.getData();

                        if (!currentSourceList.contains(positionSource)) {
                            currentSourceList.add(positionSource);
                            adapterSource.updateData(null, currentSourceList);
                            adapterSource.notifyDataSetChanged();
                        }

                        if (!customListTarget.contains(customList)) {
                            customListTarget.add(customList);
                            adapterTarget.updateData(customListTarget, new ArrayList<Integer>());
                            adapterTarget.notifyDataSetChanged();
                        }
                        if (adapterTarget.getItemCount() < 1) {
                            mListener.setEmptyList(true);
                        }

                        if (v.getId() == R.id.gallerySmall) {
                            mListener.setEmptyList(false);
                        }
                    } else if (((View) viewSource.getParent()).getId() == R.id.containerRecycle
                            && v.getId() != R.id.containerRecycle) {
                        RecyclerView source = (RecyclerView) viewSource.getParent();
                        GalleryAdapter adapterSource = (GalleryAdapter) source.getAdapter();
                        positionSource = (int) viewSource.getTag();

                        ArrayList<ImageModel> customListSource = adapterSource.getData();

                        ImageModel imageToRemove = customListSource.get(positionSource);
                        RecyclerView momentView = (RecyclerView) v.getRootView().findViewById(R.id.momentsView);
                        GalleryAdapter momentAdapter = (GalleryAdapter) momentView.getAdapter();
                        int posToRemove = momentAdapter.getData().indexOf(imageToRemove);
                        ArrayList<Integer> currentSourceList = momentAdapter.getMarkedPos();

                        if (currentSourceList.contains(posToRemove)) {
                            currentSourceList.remove(positionSource);
                            momentAdapter.updateData(null, currentSourceList);
                            momentAdapter.notifyDataSetChanged();
                        }

                        customListSource.remove(positionSource);
                        adapterSource.updateData(customListSource, new ArrayList<Integer>());
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

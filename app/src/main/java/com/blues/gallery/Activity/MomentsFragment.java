package com.blues.gallery.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blues.gallery.Adaptors.GalleryAdapter;
import com.blues.gallery.Adaptors.ImageModel;
import com.blues.gallery.CustomViews.NDSpinner;
import com.blues.gallery.EventHandlers.CustomDialogInterface;
import com.blues.gallery.EventHandlers.RecyclerItemClickListener;
import com.blues.gallery.EventHandlers.ResetInterface;
import com.blues.gallery.EventHandlers.SpinnerInteractionListener;
import com.blues.gallery.Helper.Utils;
import com.blues.gallery.R;

import java.util.ArrayList;
import java.util.Arrays;

import static com.blues.gallery.Activity.DummyActivity.IMGS;


public class MomentsFragment extends Fragment implements GalleryAdapter.Listener, ResetInterface, CustomDialogInterface {
    GalleryAdapter mAdapter;
    RecyclerView mRecyclerView;
    RecyclerView containerRecycle;
    GalleryAdapter containerAdapter;
    LinearLayout emptyListIndicator;
    ImageView dumpSpace;
    LinearLayout containerView;
    LinearLayoutManager linearLayoutManager;
    ImageView threeDotView;

    private static String ARG_ALBUM_NAME = null;
    ArrayList<Integer> currentSourceList = new ArrayList<>();
    ArrayList<ImageModel> data = new ArrayList<>();
    ArrayList<ImageModel> containerData = new ArrayList<>();
    private boolean fragmentCheck;
    private int currentActive = 0;
    private Parcelable posScrolled = null;


    private OnFragmentInteractionListener mListener;
    private NDSpinner spinner;

    private ArrayList<ImageModel> newData;
    private EditText collection_title;
    private Button saveBtn;

    public MomentsFragment() {
        // Required empty public constructor
    }

    public static MomentsFragment newInstance(String albumName) {
        MomentsFragment fragment = new MomentsFragment();
        ARG_ALBUM_NAME = albumName;
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ARG_ALBUM_NAME == null) {
            getActivity().setTitle("MOMENTS");
            for (ArrayList<ImageModel> IMG : IMGS.values()) {
                data.addAll(IMG);
            }
            fragmentCheck = true;
        } else {
            data.addAll(IMGS.get(ARG_ALBUM_NAME));
            getActivity().setTitle("ALBUMS");
            fragmentCheck = false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("fragmentCheck", fragmentCheck);
        outState.putParcelableArrayList("data", data);
        outState.putParcelableArrayList("containerData", containerData);
        outState.putInt("currentActive", currentActive);
        outState.putParcelable("posScrolled", posScrolled);
        outState.putIntegerArrayList("currentSourceList", currentSourceList);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            fragmentCheck = savedInstanceState.getBoolean("fragmentCheck", false);
            data = savedInstanceState.getParcelableArrayList("data");
            containerData = savedInstanceState.getParcelableArrayList("containerData");
            currentActive = savedInstanceState.getInt("currentActive", currentActive);
            posScrolled = savedInstanceState.getParcelable("posScrolled");
            currentSourceList = savedInstanceState.getIntegerArrayList("currentSourceList");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        posScrolled = linearLayoutManager.onSaveInstanceState();
        containerData = containerAdapter.getData();
        currentSourceList = mAdapter.getMarkedPos();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_moments, container, false);
        newData = data;
        init(layout);
        // Inflate the layout for this fragment
        return layout;
    }


    @Override
    public void onStart() {
        super.onStart();
        initializeSpinner();
        initializeRecyclerView();
        initializeContainerRecyclerView();
        initializeSmallGalleryButton();
        initializeDump();
        initializeThreeDotView();
        if (currentActive == 0) {
            containerView.setVisibility(View.VISIBLE);
        } else {
            containerView.setVisibility(View.GONE);
        }
    }

    private void init(View layout) {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        spinner = (NDSpinner) toolbar.findViewById(R.id.spinner_nav);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.momentsView);
        containerRecycle = (RecyclerView) layout.findViewById(R.id.containerRecycle);
        emptyListIndicator = (LinearLayout) layout.findViewById(R.id.gallerySmall);
        dumpSpace = (ImageView) layout.findViewById(R.id.dumpLocation);
        containerView = (LinearLayout) layout.findViewById(R.id.container);
        threeDotView = (ImageView) layout.findViewById(R.id.threeDotView);
        collection_title = (EditText) layout.findViewById(R.id.collection_title);
        saveBtn = (Button) layout.findViewById(R.id.saveBtn);
    }

    private void initializeSpinner() {
        if (fragmentCheck) {
            spinner.setVisibility(View.VISIBLE);
        } else {
            spinner.setVisibility(View.GONE);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.toolbar_spinner_item_actionbar, Arrays.asList(getResources().getStringArray(R.array.spinner_list_item_array)));
        dataAdapter.setDropDownViewResource(R.layout.toolbar_spinner_item_dropdown);
        spinner.setAdapter(dataAdapter);

        SpinnerInteractionListener listener = new SpinnerInteractionListener(this);
        spinner.setOnTouchListener(listener);

        spinner.setOnItemSelectedListener(listener);
    }

    private void initializeRecyclerView() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT || currentActive != 0 || !fragmentCheck)
            linearLayoutManager = (new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        else
            linearLayoutManager = (new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new GalleryAdapter(getActivity(), data, currentSourceList, this);
        mRecyclerView.setAdapter(mAdapter);
        if (posScrolled != null) {
            linearLayoutManager.onRestoreInstanceState(posScrolled);
        }
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        if (newData.get(position).isCheckJpeg() && fragmentCheck) {
                            if (appInstalledOrNot("com.speaktopic.selfieplus")) {
                                Intent intent = new Intent();
                                intent.setComponent(new ComponentName("com.speaktopic.selfieplus", "com.speaktopic.selfieplus.MainActivity"));
                                intent.putExtra("ImagePath", newData.get(position).getUrl());
                                startActivity(intent);
                            } else if (appInstalledOrNot("com.speaktopic.picadd")) {
                                Intent intent = new Intent();
                                intent.setComponent(new ComponentName("com.speaktopic.picadd", "com.speaktopic.picadd.MainActivity"));
                                intent.putExtra("ImagePath", newData.get(position).getUrl());
                                startActivity(intent);
                            } else {
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
                                dlgAlert.setMessage("Selfie and PicAdd have not been installed.");
                                dlgAlert.setTitle("Warning");
                                dlgAlert.setPositiveButton("Ok", null);
                                dlgAlert.create().show();
                            }
                        } else {
                            Intent intent = new Intent(getContext(), CarouselActivity.class);
                            intent.putParcelableArrayListExtra("data", mAdapter.getData());
                            intent.putExtra("pos", position);
                            intent.putExtra("overlayCheck", fragmentCheck);
                            startActivity(intent);
                        }
                    }
                }));

    }

    private void initializeContainerRecyclerView() {
        Utils utils = new Utils(getActivity());
        float px = utils.dpToPx(100);
        GridLayoutManager gridLayoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            gridLayoutManager = new GridLayoutManager(getActivity(), (int) (utils.getScreenWidth() / px - 1));
        else
            gridLayoutManager = new GridLayoutManager(getActivity(), (int) (utils.getScreenWidth() / px - 2));
        gridLayoutManager.setAutoMeasureEnabled(true);
        containerRecycle.setLayoutManager(gridLayoutManager);
        containerRecycle.setHasFixedSize(true);
        containerAdapter = new GalleryAdapter(getActivity(), containerData, new ArrayList<Integer>(), true, this);
        containerRecycle.setAdapter(containerAdapter);
        containerRecycle.setOnDragListener(containerAdapter.getDragInstance());
        if (containerData.size() > 0) {
            setEmptyList(false);
        } else {
            setEmptyList(true);
        }
    }

    private void initializeDump() {
        dumpSpace.setOnDragListener(containerAdapter.getDragInstance());
        containerView.setOnDragListener(containerAdapter.getDragInstance());
        mRecyclerView.setOnDragListener(containerAdapter.getDragInstance());
    }

    private void initializeSmallGalleryButton() {
        emptyListIndicator.setOnDragListener(containerAdapter.getDragInstance());
    }

    private void initializeThreeDotView() {
        threeDotView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context wrapper = new ContextThemeWrapper(getContext(), R.style.PopupMenu);
                PopupMenu popup = new PopupMenu(wrapper, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.actions, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.zip:
                                Toast.makeText(getActivity(), "ZIP", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.add_keyword:
                                Toast.makeText(getActivity(), "Add Keyword", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.share:
                                Toast.makeText(getActivity(), "Share", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.clear:
                                mAdapter.updateData(null, new ArrayList<Integer>());
                                mAdapter.notifyDataSetChanged();
                                containerAdapter.clearData();
                                setEmptyList(true);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    @Override
    public void setEmptyList(boolean visibility) {
        emptyListIndicator.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        threeDotView.setVisibility(!visibility ? View.VISIBLE : View.INVISIBLE);
        collection_title.setVisibility(!visibility ? View.VISIBLE : View.INVISIBLE);
        saveBtn.setVisibility(!visibility ? View.VISIBLE : View.INVISIBLE);
    }


    public void resetForAll() {
        newData = data;
        mAdapter.updateData(newData, currentSourceList);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
        if (fragmentCheck) {
            containerView.setVisibility(View.VISIBLE);
            setEmptyList(true);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                linearLayoutManager = (new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            else
                linearLayoutManager = (new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }
        currentActive = 0;
    }

    public void resetLayout(int i) {
        containerView.setVisibility(View.GONE);
        containerAdapter.clearData();
        currentActive = i;
        currentSourceList = new ArrayList<>();
        linearLayoutManager = (new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void UpdateDone(ArrayList<ImageModel> data, int pos) {
        newData = data;
        mAdapter.updateData(newData, new ArrayList<Integer>());
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public ArrayList<ImageModel> getData() {
        return data;
    }

    public NDSpinner getSpinner() {
        return spinner;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}

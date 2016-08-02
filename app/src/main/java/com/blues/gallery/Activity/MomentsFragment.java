package com.blues.gallery.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blues.gallery.Adaptors.GalleryAdapter;
import com.blues.gallery.Adaptors.ImageModel;
import com.blues.gallery.CustomViews.NDSpinner;
import com.blues.gallery.EventHandlers.RecyclerItemClickListener;
import com.blues.gallery.Helper.AppConstant;
import com.blues.gallery.Helper.Utils;
import com.blues.gallery.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import static com.blues.gallery.Activity.DummyActivity.IMGS;


public class MomentsFragment extends Fragment implements GalleryAdapter.Listener {
    GalleryAdapter mAdapter;
    RecyclerView mRecyclerView;
    RecyclerView containerRecycle;
    GalleryAdapter galleryAdapter;
    LinearLayout gallerySmall;
    ImageView dumpSpace;
    LinearLayout containerView;
    LinearLayoutManager linearLayoutManager;
    private static String ARG_ALBUM_NAME = null;

    ArrayList<ImageModel> data = new ArrayList<>();
    ArrayList<ImageModel> containerData = new ArrayList<>();

    private OnFragmentInteractionListener mListener;
    private NDSpinner spinner;
    private ArrayList<ImageModel> newData;
    private boolean fragmentCheck;
    private int currentActive = 0;
    private Parcelable posScrolled = null;

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
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        posScrolled = linearLayoutManager.onSaveInstanceState();
        containerData = galleryAdapter.getData();
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
        if (fragmentCheck && currentActive == 0) {
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
        gallerySmall = (LinearLayout) layout.findViewById(R.id.gallerySmall);
        dumpSpace = (ImageView) layout.findViewById(R.id.dumpLocation);
        containerView = (LinearLayout) layout.findViewById(R.id.container);
    }

    private void initializeSpinner() {
        spinner.setVisibility(View.VISIBLE);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.toolbar_spinner_item_actionbar, Arrays.asList(getResources().getStringArray(R.array.spinner_list_item_array)));
        dataAdapter.setDropDownViewResource(R.layout.toolbar_spinner_item_dropdown);
        spinner.setAdapter(dataAdapter);

        SpinnerInteractionListener listener = new SpinnerInteractionListener();
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
        mAdapter = new GalleryAdapter(getActivity(), data, this);
        mRecyclerView.setAdapter(mAdapter);
        if (posScrolled != null) {
            linearLayoutManager.onRestoreInstanceState(posScrolled);
        }
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        if (newData.get(position).getName().equals(AppConstant.overlayCheckText) && fragmentCheck) {
                            Toast.makeText(getActivity(), "Test", Toast.LENGTH_SHORT).show();//todo
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
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 128, r.getDisplayMetrics());
        Utils utils = new Utils(getActivity());
        GridLayoutManager gridLayoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            gridLayoutManager = new GridLayoutManager(getActivity(), (int) (utils.getScreenWidth() / px));
        else
            gridLayoutManager = new GridLayoutManager(getActivity(), (int) (utils.getScreenWidth() / px - 1));
        gridLayoutManager.setAutoMeasureEnabled(true);
        containerRecycle.setLayoutManager(gridLayoutManager);
        containerRecycle.setHasFixedSize(true);
        galleryAdapter = new GalleryAdapter(getActivity(), containerData, this);
        containerRecycle.setAdapter(galleryAdapter);
        containerRecycle.setOnDragListener(galleryAdapter.getDragInstance());
        if (containerData.size() > 0) {
            setEmptyList(false);
        }
    }

    private void initializeDump() {
        dumpSpace.setOnDragListener(galleryAdapter.getDragInstance());
    }

    private void initializeSmallGalleryButton() {
        gallerySmall.setOnDragListener(galleryAdapter.getDragInstance());
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


    private void createDatePicker(String title, final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd", Locale.US);

        final EditText input = new EditText(getActivity());
        input.setFocusable(false);
        Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog fromDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDatePickerDialog.show();
            }
        });
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newData = new ArrayList<>();
                for (ImageModel imageModel : data) {
                    String[] propSplit = imageModel.getName().split("_");
                    if (propSplit.length == 3) {
                        if (propSplit[pos].equalsIgnoreCase(input.getText().toString())) {
                            newData.add(imageModel);
                        }
                    } else if (imageModel.getName().contains(input.getText().toString()))
                        newData.add(imageModel);

                }
                mAdapter.updateData(newData);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(0);
            }
        });
        builder.setCancelable(false);
        builder.show();

    }

    private void createTextDialog(String title, final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        final EditText input = new EditText(getActivity());
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newData = new ArrayList<>();
                for (ImageModel imageModel : data) {
                    String[] propSplit = imageModel.getName().split("_");
                    if (propSplit.length == 3) {
                        if (propSplit[pos].equalsIgnoreCase(input.getText().toString())) {
                            newData.add(imageModel);
                        }
                    } else if (imageModel.getName().contains(input.getText().toString()))
                        newData.add(imageModel);

                }
                mAdapter.updateData(newData);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(0);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public void setEmptyList(boolean visibility) {
        gallerySmall.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }


    public class SpinnerInteractionListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {

        boolean userSelect = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            userSelect = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (userSelect) {
                String items = spinner.getSelectedItem().toString();
                switch (items) {
                    case "ALL":
                        resetForAll();
                        return;
                    case "DATE":
                        createDatePicker("Select Date", 1);
                        resetLayout(1);
                        return;
                    case "LOCATION":
                        createTextDialog("Select Location", 0);
                        resetLayout(2);
                        return;
                    case "EVENT":
                        createTextDialog("Select Event", 2);
                        resetLayout(3);

                }
                userSelect = false;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }

    private void resetForAll() {
        newData = data;
        mAdapter.updateData(newData);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
        if (fragmentCheck) {
            containerView.setVisibility(View.VISIBLE);
            setEmptyList(true);
            linearLayoutManager = (new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }
        currentActive = 0;

    }

    private void resetLayout(int i) {
        containerView.setVisibility(View.GONE);
        galleryAdapter.clearData();
        currentActive = i;
        linearLayoutManager = (new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setLayoutManager(linearLayoutManager);
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

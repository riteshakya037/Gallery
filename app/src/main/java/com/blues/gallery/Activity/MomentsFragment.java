package com.blues.gallery.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import java.util.List;
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

    private static String ARG_ALBUM_NAME = null;

    ArrayList<ImageModel> data = new ArrayList<>();

    private OnFragmentInteractionListener mListener;
    private NDSpinner spinner;
    private ArrayList<ImageModel> newData;
    private boolean fragmentCheck;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_moments, container, false);
        if (mRecyclerView == null) {
            newData = data;
            initializeSpinner();
            initializeRecyclerView(layout);
            initializeContainerRecyclerView(layout);
            initializeSmallGalleryButton(layout);
            initializeDump(layout);
        }
        containerView = (LinearLayout) layout.findViewById(R.id.container);
        if (fragmentCheck) {
            containerView.setVisibility(View.VISIBLE);
        } else {
            containerView.setVisibility(View.GONE);
        }
        // Inflate the layout for this fragment
        return layout;
    }


    private void initializeContainerRecyclerView(View layout) {
        containerRecycle = (RecyclerView) layout.findViewById(R.id.containerRecycle);
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 128, r.getDisplayMetrics());
        Utils utils = new Utils(getActivity());
        GridLayoutManager gridLayoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT || spinner.getSelectedItemPosition() != 0)
            gridLayoutManager = new GridLayoutManager(getActivity(), (int) (utils.getScreenWidth() / px));
        else
            gridLayoutManager = new GridLayoutManager(getActivity(), (int) (utils.getScreenWidth() / px - 1));
        gridLayoutManager.setAutoMeasureEnabled(true);
        containerRecycle.setLayoutManager(gridLayoutManager);
        containerRecycle.setHasFixedSize(true);
        galleryAdapter = new GalleryAdapter(getActivity(), new ArrayList<ImageModel>(), fragmentCheck, this);
        containerRecycle.setAdapter(galleryAdapter);
        containerRecycle.setOnDragListener(galleryAdapter.getDragInstance());
    }

    private void initializeDump(View layout) {
        dumpSpace = (ImageView) layout.findViewById(R.id.dumpLocation);
        dumpSpace.setOnDragListener(galleryAdapter.getDragInstance());
    }

    private void initializeSmallGalleryButton(View layout) {
        gallerySmall = (LinearLayout) layout.findViewById(R.id.gallerySmall);
        gallerySmall.setOnDragListener(galleryAdapter.getDragInstance());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void initializeSpinner() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        spinner = (NDSpinner) toolbar.findViewById(R.id.spinner_nav);
        spinner.setVisibility(View.VISIBLE);
        CustomSpinnerAdaptor spinnerAdapter = new CustomSpinnerAdaptor();
        spinnerAdapter.addItems(Arrays.asList(getResources().getStringArray(R.array.spinner_list_item_array)));
        spinner.setAdapter(spinnerAdapter);

        SpinnerInteractionListener listener = new SpinnerInteractionListener();
        spinner.setOnTouchListener(listener);

        spinner.setOnItemSelectedListener(listener);
    }

    private void initializeRecyclerView(View layout) {

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.momentsView);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        else
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new GalleryAdapter(getActivity(), data, fragmentCheck, this);
        mRecyclerView.setAdapter(mAdapter);

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
                mAdapter.updateData(newData, fragmentCheck);
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
                mAdapter.updateData(newData, fragmentCheck);
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
                        newData = data;
                        mAdapter.updateData(newData, fragmentCheck);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.scrollToPosition(0);
                        if (fragmentCheck) {
                            containerView.setVisibility(View.VISIBLE);
                            setEmptyList(true);
                        }
                        return;
                    case "DATE":
                        createDatePicker("Select Date", 1);
                        containerView.setVisibility(View.GONE);
                        galleryAdapter.clearData();
                        return;
                    case "LOCATION":
                        createTextDialog("Select Location", 0);
                        containerView.setVisibility(View.GONE);
                        galleryAdapter.clearData();

                        return;
                    case "EVENT":
                        createTextDialog("Select Event", 2);
                        containerView.setVisibility(View.GONE);
                        galleryAdapter.clearData();

                }
                userSelect = false;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

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

    private class CustomSpinnerAdaptor extends BaseAdapter {
        private List<String> mItems = new ArrayList<>();

        public void addItems(List<String> yourObjectList) {
            mItems.addAll(yourObjectList);
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getDropDownView(int position, View view, ViewGroup parent) {
            if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
                view = getLayoutInflater(null).inflate(R.layout.toolbar_spinner_item_dropdown, parent, false);
                view.setTag("DROPDOWN");
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getTitle(position));

            return view;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
                view = getLayoutInflater(null).inflate(R.layout.
                        toolbar_spinner_item_actionbar, parent, false);
                view.setTag("NON_DROPDOWN");
            }
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getTitle(position));
            return view;
        }

        private String getTitle(int position) {
            return position >= 0 && position < mItems.size() ? mItems.get(position) : "";
        }
    }
}

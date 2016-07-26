package com.blues.gallery.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.blues.gallery.Adaptors.GalleryAdapter;
import com.blues.gallery.Adaptors.ImageModel;
import com.blues.gallery.EventHandlers.RecyclerItemClickListener;
import com.blues.gallery.Helper.Utils;
import com.blues.gallery.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class MomentsFragment extends Fragment {
    GalleryAdapter mAdapter;
    RecyclerView mRecyclerView;
    public static HashMap<String, ArrayList<ImageModel>> IMGS;
    private static String ARG_ALBUM_NAME = null;

    ArrayList<ImageModel> data = new ArrayList<>();
    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;
    private Spinner spinner;
    private ArrayList<ImageModel> newData;

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
        Utils utils = new Utils(getActivity());
        if (ARG_ALBUM_NAME == null) {
            IMGS = utils.getFilePaths(null);
            getActivity().setTitle("MOMENTS");
        } else {
            IMGS = utils.getFilePaths(ARG_ALBUM_NAME);
            getActivity().setTitle("ALBUMS");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_moments, container, false);
        if (mRecyclerView == null) {
            for (ArrayList<ImageModel> IMG : IMGS.values()) {
                data.addAll(IMG);
            }
            newData = data;
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            spinner = (Spinner) toolbar.findViewById(R.id.spinner_nav);
            spinner.setVisibility(View.VISIBLE);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(toolbar.getContext(),
                    R.array.spinner_list_item_array, R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            SpinnerInteractionListener listener = new SpinnerInteractionListener();
            spinner.setOnTouchListener(listener);

            spinner.setOnItemSelectedListener(listener);

            mRecyclerView = (RecyclerView) layout.findViewById(R.id.momentsView);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false));
            else
                mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false));
            mRecyclerView.setHasFixedSize(true);


            mAdapter = new GalleryAdapter(getActivity(), data);
            mRecyclerView.setAdapter(mAdapter);

            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                    new RecyclerItemClickListener.OnItemClickListener() {

                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent = new Intent(getContext(), CarouselActivity.class);
                            intent.putParcelableArrayListExtra("data", newData);
                            intent.putExtra("pos", position);
                            startActivity(intent);

                        }
                    }));

        }
        // Inflate the layout for this fragment
        return layout;
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
                mAdapter.getData(newData);
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
                mAdapter.getData(newData);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(0);
            }
        });
        builder.setCancelable(false);
        builder.show();
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
                        mAdapter.getData(newData);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.scrollToPosition(0);
                        return;
                    case "DATE":
                        createDatePicker("Select Date", 1);
                        return;
                    case "LOCATION":
                        createTextDialog("Select Location", 0);
                        return;
                    case "EVENT":
                        createTextDialog("Select Event", 2);
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
}

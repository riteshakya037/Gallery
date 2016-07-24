package com.blues.gallery;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blues.gallery.Helper.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;


public class MomentsFragment extends Fragment {
    GalleryAdapter mAdapter;
    RecyclerView mRecyclerView;
    public static HashMap<String, ArrayList<ImageModel>> IMGS;
    private static String ARG_ALBUM_NAME;

    ArrayList<ImageModel> data = new ArrayList<>();
    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;

    public MomentsFragment() {
        // Required empty public constructor
        ARG_ALBUM_NAME = null;
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
        for (ArrayList<ImageModel> IMG : IMGS.values()) {
            data.addAll(IMG);
        }

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.momentsView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);


        mAdapter = new GalleryAdapter(getActivity(), data);
        mRecyclerView.setAdapter(mAdapter);

//        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
//                new RecyclerItemClickListener.OnItemClickListener() {
//
//                    @Override
//                    public void onItemClick(View view, int position) {
//
//                        Intent intent = new Intent(getContext(), DetailActivity.class);
//                        intent.putParcelableArrayListExtra("data", data);
//                        intent.putExtra("pos", position);
//                        startActivity(intent);
//
//                    }
//                }));


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

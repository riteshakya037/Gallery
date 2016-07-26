package com.blues.gallery.Activity;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blues.gallery.Adaptors.AlbumAdapter;
import com.blues.gallery.Adaptors.ImageModel;
import com.blues.gallery.EventHandlers.RecyclerItemClickListener;
import com.blues.gallery.Helper.Utils;
import com.blues.gallery.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AlbumFragment extends Fragment {
    AlbumAdapter mAdapter;
    RecyclerView mRecyclerView;
    private ArrayList<String> albumList = new ArrayList<>();

    public static HashMap<String, ArrayList<ImageModel>> IMGS;
    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set action bar title
        getActivity().setTitle("ALBUMS");
        Utils utils = new Utils(getActivity());
        IMGS = utils.getFilePaths(null);
        this.albumList = new ArrayList<>(IMGS.keySet());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_album, container, false);

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.albumView);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        else
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerView.setHasFixedSize(true);


        mAdapter = new AlbumAdapter(getActivity(), IMGS);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        MomentsFragment nextFrag = MomentsFragment.newInstance(albumList.get(position));
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragContainer, nextFrag, "")
                                .addToBackStack(null)
                                .commit();

                    }
                }));


        // Inflate the layout for this fragment
        return layout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

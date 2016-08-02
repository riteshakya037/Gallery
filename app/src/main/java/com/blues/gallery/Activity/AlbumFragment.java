package com.blues.gallery.Activity;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blues.gallery.Adaptors.AlbumAdapter;
import com.blues.gallery.CustomViews.NDSpinner;
import com.blues.gallery.EventHandlers.RecyclerItemClickListener;
import com.blues.gallery.R;

import java.util.ArrayList;

import static com.blues.gallery.Activity.DummyActivity.IMGS;

public class AlbumFragment extends Fragment {
    AlbumAdapter mAdapter;
    RecyclerView mRecyclerView;
    private ArrayList<String> albumList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set action bar title
        this.albumList = new ArrayList<>(IMGS.keySet());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("ALBUMS");
        View layout = inflater.inflate(R.layout.fragment_album, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        NDSpinner spinner = (NDSpinner) toolbar.findViewById(R.id.spinner_nav);
        spinner.setVisibility(View.GONE);
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

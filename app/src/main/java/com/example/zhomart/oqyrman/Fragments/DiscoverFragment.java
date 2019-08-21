package com.example.zhomart.oqyrman.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.zhomart.oqyrman.Models.Book;
import com.example.zhomart.oqyrman.MyApplication;
import com.example.zhomart.oqyrman.R;
import com.example.zhomart.oqyrman.SearchableActivity;
import com.example.zhomart.oqyrman.Utils.GridSpacingItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DiscoverFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiscoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "params";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static final String TAG = DiscoverFragment.class.getSimpleName();
    private static final String URL = "http://192.168.64.2/oqyrman";

    private List<Book> movieList;
    private DiscoverAdapter mAdapter;
    private LinearLayout noDataLayout;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private ArrayList<Book> foundList;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    public static String getURL() {
        return URL;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscoverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoverFragment newInstance(String param1, String param2) {
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        noDataLayout = (LinearLayout) view.findViewById(R.id.noDataLayout);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        noDataLayout.setVisibility(View.GONE);

        System.out.println(URL);

        movieList = new ArrayList<>();
        mAdapter = new DiscoverAdapter(getActivity(), movieList, URL, mParam1);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setVisibility(View.VISIBLE);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        fetchStoreItems();
        swipeRefresh.setOnRefreshListener(() -> {
            swipeRefresh.setRefreshing(true);
            fetchStoreItems();
        });

        if (mAdapter == null){
            Log.d("TEST", String.valueOf(mAdapter.getItemCount()));
            recyclerView.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void fetchStoreItems() {
        final JsonArrayRequest request = new JsonArrayRequest(URL + "/books.json",
                response -> {
                    if (response == null) {
                        Toast.makeText(getActivity(), "Couldn't fetch the store items! Please try again.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    List<Book> items = new Gson().fromJson(response.toString(), new TypeToken<List<Book>>() {
                    }.getType());

                    System.out.println(items);

                    movieList.clear();
                    movieList.addAll(items);

                    mAdapter.notifyDataSetChanged();

                    recyclerView.setVisibility(View.VISIBLE);
                    noDataLayout.setVisibility(View.GONE);
                    swipeRefresh.setRefreshing(false);
                }, error -> {
                    recyclerView.setVisibility(View.GONE);
                    noDataLayout.setVisibility(View.VISIBLE);
                    // error in getting json
                    Log.e(TAG, getString(R.string.error) + error.getMessage());
    //              Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    swipeRefresh.setRefreshing(false);
                });

        MyApplication.getInstance().addToRequestQueue(request);
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

    @Override
    public void onRefresh() {
        fetchStoreItems();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_discover, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                swipeRefresh.setRefreshing(true);
                onRefresh();
                return true;
            case R.id.action_search:
                Intent intent = new Intent(getContext(), SearchableActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

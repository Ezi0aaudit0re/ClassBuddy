package com.example.amannagpal.classbuddyv2;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.amannagpal.classbuddyv2.database.models.Files;
import com.google.common.collect.Lists;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FilesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FilesAdapter filesAdapter;
    private List<Files> files;
    private SwipeRefreshLayout mySwipeRefreshLayout = null;


    public FilesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_files, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mySwipeRefreshLayout = view.findViewById(R.id.swiperefresh);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        getFilesfromDB();

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("program", "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        getFilesfromDB();

                    }
                }
        );


        return view;


    }

    private void getFilesfromDB(){
        // Get all the files from the user
        files = MainActivity.db.filesDao().getAllFiles();


        // specify an adapter (see also next example)
        filesAdapter = new FilesAdapter(Lists.reverse(files));
        mRecyclerView.setAdapter(filesAdapter);
        mySwipeRefreshLayout.setRefreshing(false);

    }




}

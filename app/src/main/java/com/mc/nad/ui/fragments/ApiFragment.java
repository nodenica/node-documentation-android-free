package com.mc.nad.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mc.nad.R;
import com.mc.nad.adapters.ApiAdapter;
import com.mc.nad.models.ModuleLocalModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApiFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;


    public ApiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_api, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // fetch local
        fetchLocal();

        return view;
    }

    private void fetchLocal() {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<ModuleLocalModel> moduleLocalModels = realm.where(ModuleLocalModel.class).findAll();

        ApiAdapter adapter = new ApiAdapter(moduleLocalModels);
        recyclerView.setAdapter(adapter);

        moduleLocalModels.addChangeListener(new RealmChangeListener<RealmResults<ModuleLocalModel>>() {
            @Override
            public void onChange(RealmResults<ModuleLocalModel> results) {
                ApiAdapter adapter = new ApiAdapter(moduleLocalModels);
                recyclerView.setAdapter(adapter);
            }
        });


    }

}

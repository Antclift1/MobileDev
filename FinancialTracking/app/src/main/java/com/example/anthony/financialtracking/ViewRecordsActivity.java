package com.example.anthony.financialtracking;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ViewRecordsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecordViewModel mRecordViewModel;
    int days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_records);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        days = getIntent().getIntExtra("days", -1);

        mRecordViewModel = ViewModelProviders.of(this).get(RecordViewModel.class);
        mRecordViewModel.getAllRecords().observe(this, new Observer<List<Record>>() {
            @Override
            public void onChanged(@Nullable final List<Record> records) {
                // Update the cached copy of the records in the adapter.
                Collections.sort(records);
                List<Record> recordDays = new ArrayList<Record>();
                long recordsSince = -1;
                if(days>0) {
                    recordsSince = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(days);
                }

                for (Record current : records) {
                    if(current.getTimestamp() > recordsSince) {
                        //sum up by type
                        recordDays.add(current);
                    }
                }
                ((RecordAdapter) mAdapter).setRecords(recordDays);
            }
        });
        mAdapter = new RecordAdapter(this, mRecordViewModel);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public void viewAll(){
        days=-1;
        mAdapter.notifyDataSetChanged();
    }
}

package com.example.anthony.financialtracking;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.List;

/**
 * This is for testing the stuff im working on with the database
 */
public class TestActivity extends AppCompatActivity {
    TextView text;
    Button add, clear, populate, pie;
    RecordViewModel mRecordViewModel;
    LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        text = findViewById(R.id.textView2);

        mRecordViewModel = ViewModelProviders.of(this).get(RecordViewModel.class);
        mRecordViewModel.getAllRecords().observe(this, new Observer<List<Record>>() {
            @Override
            public void onChanged(@Nullable final List<Record> words) {
                // Update the cached copy of the words in the adapter.
                Record[] foo2 = new Record[10];
                Record[] foo =(Record[]) words.toArray(foo2);
                String records="";
                for(Record r: foo){
                    if(r!=null) {
                        records += "Name: " + r.getName() + " $:" + r.getAmount() + "\n";
                    }
                }
                text.setText(records);
            }
        });


        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });
        clear = findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecordViewModel.clearDatabase();
            }
        });
        populate = findViewById(R.id.populate);
        populate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecordViewModel.populateDatabase();
            }
        });
        chart = findViewById(R.id.testLine);
        pie = findViewById(R.id.makeTestPie);
        pie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecordViewModel.makeLineChart(chart, 7);

            }
        });
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        SubmitDialogFragment submitDialogFragment = SubmitDialogFragment.newInstance("Some Title");
        submitDialogFragment.show(fm, "fragment_edit_name");
    }
}

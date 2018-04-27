package com.example.anthony.financialtracking;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    String username = "";
    static final int REGISTER_REQUEST_CODE = 1;
    TextView text;
    Button button;
    RecordViewModel mRecordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);showEditDialog();
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
    }


        private void showEditDialog() {
            FragmentManager fm = getSupportFragmentManager();
            SubmitDialogFragment submitDialogFragment = SubmitDialogFragment.newInstance("Some Title");
            submitDialogFragment.show(fm, "fragment_edit_name");
        }



    }


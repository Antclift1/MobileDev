package com.example.anthony.financialtracking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{
    Button editExpense;
    Button deleteExpense;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        editExpense = (Button)findViewById(R.id.editExpense);
        deleteExpense = (Button)findViewById(R.id.deleteExpense);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == editExpense.getId()){

        }
        else if(view.getId() == deleteExpense.getId()){

        }
    }
}

package com.example.anthony.financialtracking;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.text.InputType.TYPE_CLASS_NUMBER;

public class BudgetUpdateActivity extends AppCompatActivity implements LoginResponse{

    String username;
    LinearLayout linearLayout;
    TextView[] type;
    EditText[] editvalue;
    double[] percents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_update);

        username = getIntent().getExtras().getString("Username");

        linearLayout = (LinearLayout)findViewById(R.id.budgetedit);

        Resources res = getResources();
        String[] types = res.getStringArray(R.array.record_types);
        type = new TextView[types.length];
        editvalue = new EditText[types.length];

        for(int i = 0; i < types.length; i++){
            type[i] = new TextView(this);
            editvalue[i] = new EditText(this);

            type[i].setText(types[i]);
            editvalue[i].setInputType(TYPE_CLASS_NUMBER);

            linearLayout.addView(type[i]);
            linearLayout.addView(editvalue[i]);
        }
    }

    public void onSubmit(View view){
        double sum = 0;
        percents = new double[type.length];
        for(int i = 0; i < type.length; i++) {
            percents[i] = Double.parseDouble(editvalue[i].getText().toString());
            sum += percents[i];
        }

        if(sum > 100.0)
            Toast.makeText(this, "Total can not exceed budget!", Toast.LENGTH_SHORT).show();
        else {
            new UpdateData(this, username, "budget").execute(percents);
            finish();
        }
    }


    @Override
    public void processFinish(String result) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        finish();
    }
}

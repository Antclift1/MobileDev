package com.example.anthony.financialtracking;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;

public class SetBudgetActivity extends AppCompatActivity {
    Spinner duration;
    CurrencyEditText amount;
    Button submit;
    RecordViewModel mRecordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_budget);
        duration = (Spinner) findViewById(R.id.budget_length);
        amount = (CurrencyEditText) findViewById(R.id.budget_amount);
        // Get field from view
        submit = (Button) findViewById(R.id.budget_button);

        //Sets spinner content from strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.budget, R.layout.custom_spinner_layout);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        duration.setAdapter(adapter);

        //Get record view model
        mRecordViewModel = ViewModelProviders.of(this).get(RecordViewModel.class);

        //On click listener for submit record button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBudget();
            }
        });
        SharedPreferences prefs;
        prefs = getSharedPreferences("PREF_FILE", Context.MODE_PRIVATE);
        if(prefs!=null) {
            String restoredText = prefs.getString("duration", null);
            if (restoredText != null) {
                String dur = prefs.getString("duration", "week");//"No name defined" is the default value.
                Long am = prefs.getLong("amount", 0); //0 is the default value. and it requires API 11
                if (dur.equalsIgnoreCase("week")) {
                    duration.setSelection(0);
                } else duration.setSelection(1);
                amount.setValue(am);
            }
        }
    }

    public void addBudget(){
        SharedPreferences pref = getSharedPreferences("PREF_FILE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("duration", duration.getSelectedItem().toString());
        long amount_in_cents = amount.getRawValue();
        editor.putLong("amount", amount_in_cents);
        editor.commit();
        launchHomeView();
    }

    private void launchHomeView(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

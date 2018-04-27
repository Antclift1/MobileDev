package com.example.anthony.financialtracking;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.blackcat.currencyedittext.CurrencyEditText;

/**
 * Created by Keegan on 4/26/2018.
 */

public class SubmitDialogFragment extends DialogFragment {

    private Spinner recordType;
    private EditText recordName;
    private CurrencyEditText recordAmount;
    private Button submit;
    private RecordViewModel mRecordViewModel;

    public SubmitDialogFragment(){

    }
    public static SubmitDialogFragment newInstance(String title) {
        SubmitDialogFragment frag = new SubmitDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.submit_layout, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Get spinner
        recordType = (Spinner) view.findViewById(R.id.submit_record_type);
        recordName = (EditText) view.findViewById(R.id.submit_record_name);
        // Get text enter for amount
        recordAmount = (CurrencyEditText) view.findViewById(R.id.submit_record_amount);
        // Get field from view
        submit = (Button) view.findViewById(R.id.submit_record_button);

        //Sets spinner content from strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.record_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        recordType.setAdapter(adapter);

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Default");
        getDialog().setTitle(title);

        //Get record view model
        mRecordViewModel = ViewModelProviders.of(this).get(RecordViewModel.class);

        //On click listener for submit record button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRecordFromForm();
            }
        });



        // Show soft keyboard automatically and request focus to field
        recordAmount.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    /**
     * This creates a record by pulling the data from the form, and then submits the record to the
     * database
     */
    private void makeRecordFromForm(){
        String type = recordType.getSelectedItem().toString();
        String name = recordName.getText().toString();
        long amount_in_cents = recordAmount.getRawValue();
        double amount_decimal = (double) amount_in_cents/100;
        Record record = new Record(name, amount_decimal, type);
        mRecordViewModel.insert(record);
    }
}


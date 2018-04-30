package com.example.anthony.financialtracking;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubmitRecordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SubmitRecordFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ImageView submitIcon;
    private Spinner recordType;
    private EditText recordName;
    private CurrencyEditText recordAmount;
    private Button submit;
    private RecordViewModel mRecordViewModel;


    public SubmitRecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        submitIcon = view.findViewById(R.id.submit_icon);
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

        //Get record view model
        mRecordViewModel = ViewModelProviders.of(this).get(RecordViewModel.class);

        //On click listener for submit record button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRecordFromForm();
                Toast.makeText(getContext(), "Record Submited", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
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
        clearForm();
    }

    public void clearForm(){
        recordType.setSelection(0);
        recordName.setText("Name");
        recordAmount.setValue(0);
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

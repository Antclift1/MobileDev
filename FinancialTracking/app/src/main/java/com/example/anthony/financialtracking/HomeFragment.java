package com.example.anthony.financialtracking;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class HomeFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private TextView panel1Days;
    private TextView panel2Dollars;
    private TextView panel2Cents;
    private PieChart panelPie;
    private RecordViewModel mRecordViewModel;
    private double[] record_sums;
    private LiveData<List<Record>> records;
    private Button viewRecords;
    private ImageButton daysUp;
    private ImageButton daysDown;
    //Hidden panel that is displayed when no records exist
    private LinearLayout hiddenPanel;
    private LinearLayout overviewContent;
    private Observer<List<Record>> recordObserver;
    private int days = 7;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //Get all the ids and shit
        mRecordViewModel = ViewModelProviders.of(this).get(RecordViewModel.class);
        panel1Days= view.findViewById(R.id.panel_1_days);
        panel2Dollars = view.findViewById(R.id.panel_2_dollars);
        panel2Cents = view.findViewById(R.id.panel_2_cents);
        viewRecords = view.findViewById(R.id.viewRecords);
        viewRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRecordView();
            }
        });

        panelPie= view.findViewById(R.id.home_pieChart);
        hiddenPanel = view.findViewById(R.id.no_records_panel);
        overviewContent = view.findViewById(R.id.overview_wrapper);

        daysUp = view.findViewById(R.id.daysUp);
        daysDown = view.findViewById(R.id.daysDown);

        daysUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daysUp();
            }
        });

        daysDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daysDown();
            }
        });
        records = mRecordViewModel.getAllRecords();
        //onChange runs when the dataset changes
       recordObserver = new Observer<List<Record>>() {
            @Override
            public void onChanged(@Nullable final List<Record> newData) {
                // Update the UI, in this case, a TextView.
                draw();
                draw();
            }
        };

        //Adds the observer
        records.observe(this, recordObserver);
        //TODO remove after testing
        //Popualtes the database with 30 random entries.
        //mRecordViewModel.setUpTest();
        return view;
    }



    private void daysUp(){
        switch (panel1Days.getText().toString()) {
            case "1":
                panel1Days.setText("7");
                days =(7);
                break;
            case "7":
                panel1Days.setText("14");
                days =(14);break;
            case "14":
                panel1Days.setText("30");
                days =(30);break;
            case "30":
                panel1Days.setText("90");
                days =(90);break;
            case "90":

                days =(7);break;
                default:break;
        }
        draw();
    }

    private void daysDown(){
        switch (panel1Days.getText().toString()) {
            case "1":
                //panel1Days.setText("7");
                //days =(7);
                break;
            case "7":
                panel1Days.setText("1");
                days =(1);break;
            case "14":
                panel1Days.setText("7");
                days =(7);break;
            case "30":
                panel1Days.setText("14");
                days =(14);break;
            case "90":
                panel1Days.setText("30");
                days =(30);break;
            default:break;
        }
        draw();
    }


    /**
     * Sets all the info in the panels on the home screen. Splitting it up to make future dev easier
     * if no reccords, show the hidden panel
     */
    private void draw(){
        //If we have records, hide the no records panel and show content panel
        if(mRecordViewModel.countRecords() > 0) {
            overviewContent.setVisibility(LinearLayout.VISIBLE);
            hiddenPanel.setVisibility(LinearLayout.GONE);
            setTexts();
            setGraph();

        }else {
            //No records: show no records panels and hide content
            overviewContent.setVisibility(LinearLayout.GONE);
            hiddenPanel.setVisibility(LinearLayout.VISIBLE);
        }
    }
    private void setTexts(){
            //TODO change summary by day input
        record_sums = mRecordViewModel.getSums(Integer.parseInt(panel1Days.getText().toString()));
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String s = formatter.format(record_sums[record_sums.length - 1]);
        String dollars = s.substring(0, s.length()-3);
        String cents = s.substring(s.length()-3, s.length());
        panel2Dollars.setText(dollars);
        panel2Cents.setText(cents);

    }

    private void setGraph(){
        mRecordViewModel.makePieChart(panelPie, days);
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

    private void launchRecordView(){
        Intent intent = new Intent(this.getContext(), ViewRecordsActivity.class);
        intent.putExtra("days", days);
        startActivity(intent);
    }

}

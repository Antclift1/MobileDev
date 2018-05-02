package com.example.anthony.financialtracking;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
    private TextView daySmall;
    private TextView budgetTop;
    private TextView budgetBottom;
    private ConstraintLayout budgetPanel;
    private ProgressBar bar;


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

        budgetPanel = view.findViewById(R.id.panel_budget);
        budgetTop=view.findViewById(R.id.budget_text1);
        budgetBottom=view.findViewById(R.id.budget_text2);
        bar = view.findViewById(R.id.progress);

        daysUp = view.findViewById(R.id.daysUp);
        daysDown = view.findViewById(R.id.daysDown);
        daySmall = view.findViewById(R.id.panel_1_text2);

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

    public void checkBudget(){
        SharedPreferences prefs = getContext().getSharedPreferences("PREF_FILE", Context.MODE_PRIVATE);
        String restoredText = prefs.getString("duration", null);
        if (restoredText != null) {
            String duration = prefs.getString("duration", "week");//"No name defined" is the default value.
            Long amount = prefs.getLong("amount", 0); //0 is the default value. and it requires API 11
            setBudget(duration, amount);
        }
    }


    public void setBudget(String budget, long amount){
        if(amount==0){
            budgetPanel.setVisibility(LinearLayout.GONE);
            return;
        }
        budgetPanel.setVisibility(LinearLayout.VISIBLE);
        if(budget.equals("Week")){
            double[] sums = mRecordViewModel.getSums(7);
            double total = sums[sums.length-1];
            double amount_dollars = amount/100;
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String total_string = formatter.format(total);
            String budget_string = formatter.format(amount_dollars);
            String top = "You budgeted " + budget_string + " this week: ";
            budgetTop.setText(top);
            String bottom = "You spent " + total_string;
            budgetBottom.setText(bottom);
            int progress = ((int) total) / (int) amount_dollars;
            bar.setMax((int) amount_dollars);
            bar.setProgress((int) total);

        }

        if(budget.equals("Month")){
            double[] sums = mRecordViewModel.getSums(30);
            double total = sums[sums.length-1];
            double amount_dollars = amount/100;
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String total_string = formatter.format(total);
            String budget_string = formatter.format(amount_dollars);
            String top = "You budgeted " + budget_string + " this month: ";
            budgetTop.setText(top);
            String bottom = "You spent " + total_string;
            budgetBottom.setText(bottom);
            bar.setMax((int) amount_dollars);
            bar.setProgress((int) total);

        }

    }

    private void daysUp(){
        switch (panel1Days.getText().toString()) {
            case "1":
                panel1Days.setText("7");
                daySmall.setText("days");
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
                break;
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
                daySmall.setText("day");
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
        checkBudget();
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

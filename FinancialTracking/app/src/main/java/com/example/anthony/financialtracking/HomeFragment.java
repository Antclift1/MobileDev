package com.example.anthony.financialtracking;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private TextView panel1Amount;
    private TextView panel3Text;
    private PieChart panelPie;
    private RecordViewModel mRecordViewModel;
    private double[] record_sums;
    private LiveData<List<Record>> records;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecordViewModel = ViewModelProviders.of(this).get(RecordViewModel.class);
        panel1Days= view.findViewById(R.id.panel_1_days);
        panel1Amount= view.findViewById(R.id.panel_1_amount);
        panel3Text= view.findViewById(R.id.panel_3_text);
        panelPie= view.findViewById(R.id.panel_2_pie);
        records = mRecordViewModel.getAllRecords();
        final Observer<List<Record>> recordObserver = new Observer<List<Record>>() {
            @Override
            public void onChanged(@Nullable final List<Record> newData) {
                // Update the UI, in this case, a TextView.
                setTexts();
                setGraph();
            }
        };
        records.observe(this, recordObserver);
        mRecordViewModel.setUpTest();
        return view;
    }

    private void setTexts(){
        //TODO change summary by day input
        record_sums = mRecordViewModel.getSums(-1);
        Double amount = BigDecimal.valueOf(record_sums[record_sums.length-1])
                .setScale(3, RoundingMode.HALF_UP)
                .doubleValue();
        String s= "You have spent: "+amount;
        panel1Amount.setText(s);
    }

    private void setGraph(){
        mRecordViewModel.makePieChart(panelPie);
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

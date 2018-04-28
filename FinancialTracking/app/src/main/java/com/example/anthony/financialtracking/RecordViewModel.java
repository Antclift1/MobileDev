package com.example.anthony.financialtracking;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

/**
 * Created by Keegan on 4/26/2018.
 */

public class RecordViewModel extends AndroidViewModel {
    private RecordRepository mRepository;
    private LiveData<List<Record>> mAllRecords;
    private String[] types;
    private Context context;
    ChartMaker maker;


    public RecordViewModel(Application application) {
        super(application);
        context = application.getApplicationContext();
        mRepository = new RecordRepository(application);
        mAllRecords = mRepository.getAllRecords();
        types = context.getResources().getStringArray(R.array.record_types);
        maker = new ChartMaker(this);
    }

    LiveData<List<Record>> getAllRecords() {
        return mAllRecords;
    }

    public void insert(Record record) {
        mRepository.insert(record);
    }

    public void clearDatabase(){
        mRepository.clearDatabase();
    }

    public void populateDatabase(){
        mRepository.populateDatabase(types);
    }

    public void makeLineChart(LineChart line, int numDays){
        maker.makeLineChart(line, numDays);
    }

    public void makePieChart(PieChart pie){
        maker.makePieChart(pie);
    }


    public String[] getTypes() {
        return types;
    }

    public Context getContext() {
        return context;
    }
}

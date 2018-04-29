package com.example.anthony.financialtracking;

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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

/**
 * Created by Keegan on 4/27/2018.
 */

public class ChartMaker {
    private RecordViewModel mRVM;
    String[] types;
    Context context;

    public ChartMaker(RecordViewModel m){
        mRVM = m;
        types=mRVM.getTypes();
        context=mRVM.getContext();
    }

    /**
     * Generates a set of data for a pie chart, with sum of all expenses by type
     * Use
     * pieChart.setData(data);
     * pieChart.invalidate(); // refresh
     * with the returned data to set your pie chart
     * @return pie chart data
     */
    public void makePieChart(PieChart pie){
        LiveData<List<Record>> record_live= mRVM.getAllRecords();
        List<Record> records_list = record_live.getValue();
        List<PieEntry> entries = new ArrayList<>();

        double[] sums = sumByType(records_list);
        for(int i=0; i<types.length; i++){
            entries.add(new PieEntry((float) sums[i], types[i]));
        }
        PieDataSet set = new PieDataSet(entries, "Expenses");
        int[]colors = context.getResources().getIntArray(R.array.rainbow);
        set.setColors(colors);
        PieData data = new PieData(set);
        pie.setData(data);
        pie.setUsePercentValues(true);
        pie.setCenterText("Expenses by Type");
        pie.invalidate();
    }

    public void makeLineChart(LineChart line, int numDays){
        LiveData<List<Record>> record_live= mRVM.getAllRecords();
        List<Record> records_list = record_live.getValue();
        if(records_list == null){
            return;
        }
        //Sort record by timestamp
        Collections.sort(records_list);
        //Set up lists of entry lists, 1 for each type
        List<List<Entry>> type_lists = new ArrayList<>();
        for(int i=0; i<types.length; i++){
            List<Entry> entries = new ArrayList<>();
            type_lists.add(entries);
        }
        //gets the time value for input number of days ago
        long recordsSince = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(numDays);
        //will store the sums by type
        double[] sums = new double[types.length];
        java.util.Arrays.fill(sums, 0);
        //iterate over all records
        ListIterator<Record> itr = records_list.listIterator();
        while(itr.hasNext()){
            Record current = itr.next();
            //if in time span we want
            if(current.getTimestamp() > recordsSince) {
                int index = getIndex(current.getType());
                sums[index] += current.getAmount();
                //time  since start of time period
                float timeRelative = (current.getTimestamp() -  recordsSince);
                //add to entry list at same index as type string
                type_lists.get(index).add(new Entry(timeRelative, (float) sums[index]));
            }
            else{
                break;
            }
        }
        //Set up the line data set
        List<ILineDataSet> data_sets = new ArrayList<>();
        int[]colors = context.getResources().getIntArray(R.array.rainbow);
        for(int i=0; i<types.length; i++){
            LineDataSet set = new LineDataSet(type_lists.get(i), types[i]);
            set.setColor(colors[i]);
            set.setCircleColor(colors[i]);
            data_sets.add(set);

        }
        //sets up the data object
        LineData data =  new LineData(data_sets);
        data.setValueTextSize(0);

        YAxis left = line.getAxisLeft();
        YAxis right = line.getAxisRight();
        left.setEnabled(false);
        right.setAxisMinimum(0);


        line.setBackgroundColor(Color.WHITE);
        line.setData(data);
        XAxis top = line.getXAxis();
        top.setValueFormatter(new DayAxisValueFormatter(line, numDays));
        right.setValueFormatter(new MyYAxisValueFormatter());
        line.invalidate();

    }

    private double[] sumByType(List<Record> records_list){
        double[] sums = new double[types.length];
        java.util.Arrays.fill(sums, 0);
        ListIterator<Record> itr = records_list.listIterator();
        while(itr.hasNext()){
            Record current = itr.next();
            sums[getIndex(current.getType())] += current.getAmount();
        }
        return sums;
    }

    private int getIndex(String s){
        for(int i=0; i< types.length; i++){
            if(s.equals(types[i])){
                return i;
            }
        }
        return -1;
    }

    /**
     * Formats the y axis of the chart into
     */
    private class MyYAxisValueFormatter implements IAxisValueFormatter {
        DecimalFormat mFormat;

        public MyYAxisValueFormatter() {

            // format values to 1 decimal digit
            mFormat = new DecimalFormat("###,###,##0.00");
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            return "$" + mFormat.format(value);
        }

        /** this is only needed if numbers are returned, else return 0 */

        public int getDecimalDigits() { return 0; }
    }
}

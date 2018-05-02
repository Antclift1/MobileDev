package com.example.anthony.financialtracking;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;

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
import java.util.Calendar;
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
    private String[] types;
    private LiveData<List<Record>> liveData;
    private LiveData<List<Record>> liveData7;
    //TODO fix this
    private Context context;
    private ChartMaker maker;


    public RecordViewModel(Application application) {
        super(application);
        context = application.getApplicationContext();
        mRepository = new RecordRepository(application);
        types = context.getResources().getStringArray(R.array.record_types);
        liveData =  mRepository.getAllRecords();
        maker = new ChartMaker(this);
    }

    LiveData<List<Record>> getAllRecords() {
        return liveData;
    }


    LiveData<List<Record>> getAllRecords(int days) {
        long currentTime = System.currentTimeMillis();
        long timeDaysAgo = currentTime - TimeUnit.DAYS.toMillis(days);
        return mRepository.getAllRecords(timeDaysAgo);
    }


    public void insert(Record record) {
        mRepository.insert(record);
    }

    public void delete(Record record){mRepository.delete(record);}
    public void update(Record record){mRepository.update(record);}

    public void clearDatabase(){
        mRepository.clearDatabase();
    }

    public void populateDatabase(){
        mRepository.populateDatabase(types);
    }

    public void makeLineChart(LineChart line, int numDays){
        maker.makeLineChart(line, numDays);
    }

    public void makePieChart(PieChart pie, int days){
        maker.makePieChart(pie, days);
    }

    public int countRecords(){
        List<Record> list = liveData.getValue();
        if(list != null){
            return list.size();
        }
        return 0;

    }


    public String[] getTypes() {
        return types;
    }

    /**
     * Return an array of length types.length+1. The sum for each type is stored in
     * the same index as type name. the last double is the total sum
     *
     * Records up to numdays old are included in the sum
     *
     * call with numDays = -1 to get sums of all records
     */


    public double[] getSums(int numDays) {
        double[] sums = new double[types.length + 1];
        java.util.Arrays.fill(sums, 0);
        //get the records
        List<Record> records_list = liveData.getValue();
        //sort the records
        if (records_list == null) {
            java.util.Arrays.fill(sums, -1);
            return sums;
        }
        Collections.sort(records_list);

        //gets the time value for input number of days ago
        long recordsSince = -1;
        if(numDays>0) {
            recordsSince = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(numDays);
        }

        for (Record current : records_list) {
            if(current.getTimestamp() > recordsSince) {
                //sum up by type
                sums[getIndex(current.getType())] += current.getAmount();
                //sum up total
                sums[sums.length-1] += current.getAmount();
            }
        }
        return sums;
    }




    public int getIndex(String s){
        for(int i=0; i< types.length; i++){
            if(s.equals(types[i])){
                return i;
            }
        }
        return -1;
    }

    public void setUpTest(){
        List<Record> records_list = liveData.getValue();
        //sort the records
        if (records_list == null || records_list.isEmpty()) {
            populateDatabase();
        }
    }

    public Context getContext() {
        return context;
    }

    public long getOldest(){
        List<Record> list = new ArrayList<>(liveData.getValue());
        Collections.sort(list);
        return list.get(0).getTimestamp();
    }

    public List<double[]> getSumsByWeek() {
        long oldest = getOldest();
        float weeks = oldest % TimeUnit.DAYS.toMillis(7);
        // get today and clear time of day
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        // get start of this week in milliseconds
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        long startOfWeek = cal.getTimeInMillis();
        List<double[]> sumsAll = new ArrayList<>();

        List<Record> list = new ArrayList<>(liveData.getValue());
        for (long l = startOfWeek; l > oldest; l -= TimeUnit.DAYS.toMillis(7)) {
            double[] sums = new double[types.length + 1];
            for (Record current : list) {
                if (current.getTimestamp() < startOfWeek && current.getTimestamp() > startOfWeek - TimeUnit.DAYS.toMillis(7)) {
                    //sum up by type
                    sums[getIndex(current.getType())] += current.getAmount();
                    //sum up total
                    sums[sums.length - 1] += current.getAmount();
                }
            }
            sumsAll.add(sums);

        }
        return sumsAll;
    }
}

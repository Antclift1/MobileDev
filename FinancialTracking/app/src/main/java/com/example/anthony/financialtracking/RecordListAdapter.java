package com.example.anthony.financialtracking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Keegan on 4/26/2018.
 */

public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.RecordViewHolder> {

    class RecordViewHolder extends RecyclerView.ViewHolder {
        private final TextView recordItemView;

        private RecordViewHolder(View itemView) {
            super(itemView);
            recordItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater mInflater;
    private List<Record> mRecords; // Cached copy of records

    RecordListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new RecordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecordViewHolder holder, int position) {
        if (mRecords != null) {
            Record current = mRecords.get(position);
            holder.recordItemView.setText("name:" +current.getName()+" $:"+current.getAmount());
        } else {
            // Covers the case of data not being ready yet.
            holder.recordItemView.setText("No Record");
        }
    }

    void setRecords(List<Record> records){
        mRecords = records;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mRecords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mRecords != null)
            return mRecords.size();
        else return 0;
    }
}
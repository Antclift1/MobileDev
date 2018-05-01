package com.example.anthony.financialtracking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Keegan on 4/30/2018.
 */

public class RecordAdapter extends
    RecyclerView.Adapter<RecordAdapter.RecordViewHolder>
    {


        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class RecordViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView mName;
            public TextView mAmount;
            public TextView mType;
            public TextView mDate;

            public RecordViewHolder(View itemView) {
                super(itemView);
                mName = itemView.findViewById(R.id.itemName);
                mAmount = itemView.findViewById(R.id.itemAmount);
                mType = itemView.findViewById(R.id.itemType);
                mDate = itemView.findViewById(R.id.itemDate);
            }
        }
        private final LayoutInflater mInflater;
        private List<Record> mRecords; // Cached copy of records


        // Provide a suitable constructor (depends on the kind of dataset)
    public RecordAdapter(Context context){
        mInflater = LayoutInflater.from(context);
    }

        // Create new views (invoked by the layout manager)
        @Override
        public RecordAdapter.RecordViewHolder onCreateViewHolder (ViewGroup parent,int viewType){
            View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
            return new RecordViewHolder(itemView);
    }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder (RecordViewHolder holder,int position){
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
            if (mRecords != null) {
                Record current = mRecords.get(position);
                String name = current.getName();
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                String amount = formatter.format(current.getAmount());
                SimpleDateFormat sf = new SimpleDateFormat("MM-dd-yyyy");
                Date date = new Date(current.getTimestamp());
                holder.mName.setText(current.getName());
                holder.mAmount.setText(amount);
                holder.mType.setText(current.getType());
                holder.mDate.setText(sf.format(date));
            } else {
                // Covers the case of data not being ready yet.
                holder.mName.setText("No Records");
            }

    }
        public void setRecords(List<Record> records){
            mRecords = records;
            notifyDataSetChanged();
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            if (mRecords != null)
                return mRecords.size();
            else return 0;
        }

}

package com.example.anthony.financialtracking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.blackcat.currencyedittext.CurrencyEditText;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Keegan on 4/30/2018.
 *
 * Makes the view all records views from the record live data, by making recyler view holders
 */

public class RecordAdapter extends
    RecyclerView.Adapter<RecordAdapter.RecordViewHolder>
    {


        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class RecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            // each data item is just a string in this case
            public Record record;
            TextView mName;
            TextView mAmount;
            TextView mType;
            TextView mDate;


            GridLayout edit;
            TextView mEditName;
            CurrencyEditText mEditAmount;
            Spinner mEditType;
            Button save;
            Button delete;
            RecordViewModel holderRecordViewModel;

            RecordViewHolder(View itemView, RecordViewModel recordViewModel)  {
                super(itemView);
                holderRecordViewModel=recordViewModel;
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                mName = itemView.findViewById(R.id.itemName);
                mAmount = itemView.findViewById(R.id.itemAmount);
                mType = itemView.findViewById(R.id.itemType);
                mDate = itemView.findViewById(R.id.itemDate);

                edit = itemView.findViewById(R.id.editGrid);
                mEditName = itemView.findViewById(R.id.edit_record_name);
                mEditAmount = itemView.findViewById(R.id.edit_record_amount);
                mEditType = itemView.findViewById(R.id.edit_record_type);
                save = itemView.findViewById(R.id.save_edit);
                delete = itemView.findViewById(R.id.delete_edit);

            }

            @Override
            public void onClick(View view) {
                // Context context = view.getContext();
                // article.getName()
            }

            @Override
            public boolean onLongClick(View view) {
                if(edit.getVisibility() == View.GONE){
                    mEditName.setText(mName.getText());
                    mEditAmount.setText(mAmount.getText());
                    //Sets spinner content from strings.xml
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mEditType.getContext(),
                            R.array.record_types, R.layout.custom_spinner_layout);
                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    mEditType.setAdapter(adapter);
                    int spinnerPosition = adapter.getPosition(mType.getText());
                    mEditType.setSelection(spinnerPosition);
                    edit.setVisibility(View.VISIBLE);

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            record.setName(mEditName.getText().toString());
                            long amount_in_cents = mEditAmount.getRawValue();
                            double amount_decimal = (double) amount_in_cents/100;
                            record.setAmount(amount_decimal);
                            record.setType(mEditType.getSelectedItem().toString());
                            holderRecordViewModel.update(record);
                            edit.setVisibility(View.GONE);
                        }
                    });

                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holderRecordViewModel.delete(record);
                        }
                    });
                }
                else{
                    edit.setVisibility(View.GONE);
                }
                return true;
            }
        }
        private final LayoutInflater mInflater;
        private RecordViewModel mRecordViewModel;
        private List<Record> mRecords; // Cached copy of records


        // Provide a suitable constructor (depends on the kind of dataset)
    RecordAdapter(Context context, RecordViewModel recordViewModel){
        mInflater = LayoutInflater.from(context);
        mRecordViewModel = recordViewModel;
    }

        // Create new views (invoked by the layout manager)
        @Override
        public RecordAdapter.RecordViewHolder onCreateViewHolder (ViewGroup parent,int viewType){
            View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
            return new RecordViewHolder(itemView, mRecordViewModel);
    }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder (RecordViewHolder holder,int position){
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
            if (mRecords != null) {
                holder.record = mRecords.get(position);
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                String amount = formatter.format(holder.record.getAmount());
                SimpleDateFormat sf = new SimpleDateFormat("MM-dd-yyyy");
                Date date = new Date(holder.record.getTimestamp());
                holder.mName.setText(holder.record.getName());
                holder.mAmount.setText(amount);
                holder.mType.setText(holder.record.getType());
                holder.mDate.setText(sf.format(date));
                holder.holderRecordViewModel=mRecordViewModel;
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

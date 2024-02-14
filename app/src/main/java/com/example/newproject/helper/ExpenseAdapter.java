package com.example.newproject.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.newproject.R;

import java.util.List;

public class ExpenseAdapter extends ArrayAdapter<Expense> {
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

    public ExpenseAdapter(Context context, List<Expense> dataList){
        super(context, 0, dataList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, ViewGroup parent){
        Expense item = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.expense_cell, parent, false);
        }

        TextView title = convertView.findViewById(R.id.ex_title);
        TextView type = convertView.findViewById(R.id.ex_type);
        TextView cost = convertView.findViewById(R.id.ex_cost);
        TextView date = convertView.findViewById(R.id.ex_date);

        title.setText(item.getTitle());
        type.setText(String.valueOf(item.getType()));
        cost.setText(String.valueOf(item.getCost()));
        date.setText(dateFormat.format(item.getDate()));

        return convertView;
    }
}

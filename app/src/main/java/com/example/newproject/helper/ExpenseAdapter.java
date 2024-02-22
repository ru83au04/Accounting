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
        TextView cost = convertView.findViewById(R.id.ex_cost);

        title.setText(item.getTitle());
        cost.setText(String.valueOf(item.getCost()));

        return convertView;
    }

    public String typeToString(Type type){
        switch(type){
            case EAT: return "飲食";
            case CLOTHES: return "服飾";
            case LIVE: return "日常生活";
            case TRAFFIC: return "交通";
            case HOBBY: return "娛樂";
            case OTHER: return "其他";
        }
        return "";
    }
}

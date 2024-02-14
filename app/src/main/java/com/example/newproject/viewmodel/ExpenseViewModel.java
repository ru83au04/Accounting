package com.example.newproject.viewmodel;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.example.newproject.database.DatabaseHelper;
import com.example.newproject.helper.Expense;

public class ExpenseViewModel extends ViewModel {
    private DatabaseHelper databaseHelper;

    public ExpenseViewModel(Context context){
        databaseHelper = new DatabaseHelper(context);
    }

    public void insertData(Expense expense){
        databaseHelper.insertData(expense);
    }

}

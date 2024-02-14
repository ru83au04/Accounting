package com.example.newproject.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ExpenseManager {
    private Context context;
    private ArrayList<Expense> expenseList;

    public ExpenseManager(Context context){
        this.context = context;
        this.expenseList = new ArrayList<>();
    }

    public ArrayList<Expense> getExpenseList(){
        return expenseList;
    }

    public void addExpense(Expense expense){
        expenseList.add(expense);
    }

    public void sortByCost(boolean reverse){
        Comparator<Expense> byCost = Comparator.comparing(Expense::getCost);
        if(!reverse){
            expenseList.sort(byCost);
        }else{
            expenseList.sort(byCost.reversed());
        }
    }

    public void sortByType(boolean reverse){
        Comparator<Expense> byType = Comparator.comparing(Expense::getType);
        if(!reverse){
            expenseList.sort(byType);
        }else{
            expenseList.sort(byType.reversed());
        }

    }

    public void sortByDate(boolean reverse){
        Comparator<Expense> byDate = Comparator.comparing(Expense::getDate);
        if(!reverse){
            expenseList.sort(byDate);
        }else{
            expenseList.sort(byDate.reversed());
        }
    }

}

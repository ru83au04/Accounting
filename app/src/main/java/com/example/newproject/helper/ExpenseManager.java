package com.example.newproject.helper;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    public Expense createExpense (String expTitle, String stringExpCost,
                                 String stringExpType, String stringExpDate,
                                 String expDesc) throws Exception{

        // 處理title
        if(expTitle.isEmpty()){
           throw new Exception("Please enter the title");
        }
        // 處理cost
        double expCost = 0;
        if(stringExpCost.isEmpty()){
            throw new Exception("Please enter the cost");
        }else{
            try{
                expCost = Double.parseDouble(stringExpCost);
            }catch(NumberFormatException e){
                throw new Exception("Cost must be number");
            }
        }

        //處理date
        Date expDate = null;
        SimpleDateFormat format =  new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        try{
            expDate = format.parse(stringExpDate);
        }catch(ParseException e){
            throw new Exception("Date is illegal");
        }

        // 處理type
        Type expType = Type.valueOf(stringExpType);

        // 建立Expense
        Expense expense = new Expense(expTitle, expCost, expType, expDate, expDesc);
        expenseList.add(expense);
        return expense;
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

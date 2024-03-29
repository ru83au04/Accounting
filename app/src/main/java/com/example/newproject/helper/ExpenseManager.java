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
    public static final ArrayList<Expense> expenseList = new ArrayList<>();

    public ExpenseManager(Context context){
        this.context = context;
    }

    public void addExpense(Expense expense){
        expenseList.add(expense);
    }
    public void deleteExpense(Expense expense){
        expenseList.remove(expense);
    }
    public void updateExpense(Expense expense){
        expenseList.removeIf(e -> e.getId() == expense.getId());
        expenseList.add(expense);
    }

    public Expense createExpense (String expTitle, String stringExpCost,
                                 String stringExpType, String stringExpDate,
                                 String expDesc) throws Exception{

        // 處理title
        if(expTitle.isEmpty()){
           throw new Exception("請輸入名稱");
        }
        // 處理cost
        double expCost = 0;
        if(stringExpCost.isEmpty()){
            throw new Exception("請輸入金額");
        }else{
            try{
                expCost = Double.parseDouble(stringExpCost);
            }catch(NumberFormatException e){
                throw new Exception("金額必須為數字");
            }
        }
        //處理date
        Date expDate = null;
        SimpleDateFormat format =  new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        try{
            expDate = format.parse(stringExpDate);
        }catch(ParseException e){
            throw new Exception("日期格式錯誤");
        }
        // 處理type
        Type expType = typeSelect(stringExpType);
        // 建立Expense
        Expense expense = new Expense(expTitle, expCost, expType, expDate, expDesc);
        return expense;
    }

    public Type typeSelect(String type){
        switch(type){
            case "飲食": return Type.EAT;
            case "服飾": return Type.CLOTHES;
            case "日常生活": return Type.LIVE;
            case "交通": return Type.TRAFFIC;
            case "娛樂": return Type.HOBBY;
            default: return Type.OTHER;
        }
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

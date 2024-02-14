package com.example.newproject;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.newproject.database.DatabaseHelper;
import com.example.newproject.helper.Expense;
import com.example.newproject.helper.Type;
import com.example.newproject.viewmodel.ExpenseViewModel;

import java.util.Calendar;

public class CreateFragment extends Fragment {

    private View mainView;
    private DatabaseHelper databaseHelper;
    private DatePickerDialog datePickerDialog;
    private Button date;
    private EditText cost, title, description;
    private Spinner type;

    public static CreateFragment newInstance() {
        return new CreateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 將布局檔案(xml)套用到父容器中(container，通常為創建該Fragment的Activity)
        mainView = inflater.inflate(R.layout.fragment_create, container, false);

        // 獲取Activity的DatabaseHelper
        databaseHelper = ((MainActivity)requireActivity()).getDatabaseHelper();

        // 以下為頁面元件設定



        // 設定type Spinner的內容選項
        type = mainView.findViewById(R.id.type);
        ArrayAdapter<CharSequence> typeList = ArrayAdapter.createFromResource(
                getContext(), R.array.typeList, android.R.layout.simple_spinner_item);
        typeList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(typeList);

        // 設定date Button的內容，並初始化為當天日期
        date = mainView.findViewById(R.id.date);
        initDatePicker();
        date.setOnClickListener(v -> openDatePicker());

        // 設定剩餘EditText
        cost = mainView.findViewById(R.id.cost);
        title = mainView.findViewById(R.id.title);
        description = mainView.findViewById(R.id.description);

        // 設定ADD Button，同時設定其觸發邏輯
        Button createButton = mainView.findViewById(R.id.create);
        createButton.setOnClickListener(v -> createExpense());

        return mainView;
    }

    /**
     * 將頁面輸入資訊建立Expense實例，透過expenseViewModel存入DB
     */
    public void createExpense() {
        Toast createResult;
        String expTitle = title.getText().toString();
        String stringExpCost = cost.getText().toString();
        String stringExpType = type.getSelectedItem().toString();
        String expDate = date.getText().toString();
        String expDesc = "" + description.getText().toString();

        if(expTitle.isEmpty()){
            Toast.makeText(getContext(),
                    "Please enter the Title", Toast.LENGTH_SHORT).show();
            return;
        }

        double expCost = 0;
        if(stringExpCost.isEmpty()){
            Toast.makeText(getContext(),
                    "Please enter the Cost", Toast.LENGTH_SHORT).show();
            return;
        }else{
            try{
                expCost = Double.parseDouble(stringExpCost);
            }catch(NumberFormatException e){
                Toast.makeText(getContext(),
                        "Cost must be number", Toast.LENGTH_SHORT).show();
            }
        }

        Type expType = Type.valueOf(stringExpType);

        Expense expense = new Expense(expTitle, expCost, expType, expDate, expDesc);

        try {
            Expense.expenseArrayList.add(expense);
            databaseHelper.insertData(expense);
            cost.setText("");
            title.setText("");
            description.setText("");
            type.setSelection(0);
            createResult = Toast.makeText(getContext(),
                    "Create Success", Toast.LENGTH_SHORT);
            createResult.show();
        } catch (NullPointerException e) {
            Toast errorEnter = Toast.makeText(getContext(),
                    "Create Error", Toast.LENGTH_LONG);
            errorEnter.show();
            Log.d("Create Error",e.toString());
        }

    }

    /**
     * 初始化date Button的方法
     */
    public void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String date = makeDateString(month, day, year);
                        CreateFragment.this.date.setText(date);
                    }
                };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String today = makeDateString(month, day, year);
        int style = 2;
        date.setText(today);
        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
    }

    public String makeDateString(int month, int day, int year){
        month += 1;
        return month + "-" + day + "-" + year;
    }
    public void openDatePicker() {
        datePickerDialog.show();
    }
}

package com.example.newproject;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.example.newproject.helper.ExpenseManager;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExpenseFragment extends Fragment {

    private Expense expense;
    private DatePickerDialog datePickerDialog;
    private DatabaseHelper databaseHelper;
    private ExpenseManager expenseManager;
    private View mainView;
    private EditText title, cost, description;
    private Spinner type;
    private Button date, edit, delete;
    private boolean editControl;
    private long id;

    public ExpenseFragment(Expense expense){
        this.expense = expense;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_expense, container, false);

        databaseHelper = ((MainActivity)requireActivity()).getDatabaseHelper();

        expenseManager = ((MainActivity)requireActivity()).getExpenseManager();

        editControl = false;

        id = expense.getId();

        title = mainView.findViewById(R.id.expense_title);
        title.setText(expense.getTitle());
        title.setEnabled(editControl);

        cost = mainView.findViewById(R.id.expense_cost);
        cost.setText(String.valueOf(expense.getCost()));
        cost.setEnabled(editControl);

        description = mainView.findViewById(R.id.expense_description);
        description.setText(expense.getDescription());
        description.setEnabled(editControl);

        date = mainView.findViewById(R.id.expense_date);
        initDatePicker();
        date.setText(dateToString(expense.getDate()));
        date.setOnClickListener(v -> openDatePicker());
        date.setEnabled(editControl);

        type = mainView.findViewById(R.id.expense_type);
        ArrayAdapter<CharSequence> typeList = ArrayAdapter.createFromResource(
                getContext(), R.array.type_list, android.R.layout.simple_spinner_item);
        typeList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(typeList);
        type.setSelection(typeList.getPosition(expense.getType().toString()));
        type.setEnabled(editControl);

        edit = mainView.findViewById(R.id.edit);
        edit.setOnClickListener(v -> setEdit());

        return mainView;
    }


    public void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String date = makeDateString(month, day, year);
                        ExpenseFragment.this.date.setText(date);
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

    private String dateToString(Date date){
        SimpleDateFormat format =  new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        return format.format(date);
    }

    private Date stringToDate(String date){
        SimpleDateFormat format =  new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        try{
            return format.parse(date);
        }catch(ParseException e){
            Toast.makeText(getContext(),
                    "Date is illegal", Toast.LENGTH_SHORT).show();
            return new Date();
        }
    }

    public void openDatePicker() {
        datePickerDialog.show();
    }

    private void setEdit() {
        editControl = !editControl;
        edit.setText(editControl ? "Confirm" : "Edit");

        title.setEnabled(editControl);
        cost.setEnabled(editControl);
        description.setEnabled(editControl);
        date.setEnabled(editControl);
        type.setEnabled(editControl);

        if(!edit.getText().toString().contains("Confirm")){
            String expTitle = title.getText().toString();
            String stringExpCost = cost.getText().toString();
            String stringExpType = type.getSelectedItem().toString();
            String stringExpDate = date.getText().toString();
            String expDesc = "" + description.getText().toString();

            try{
                Expense expense = expenseManager.createExpense(expTitle, stringExpCost,
                        stringExpType, stringExpDate, expDesc);
                expense.setId(id);
                databaseHelper.updateExpenseInDB(expense);
                Toast.makeText(getContext(),
                        "Update Success", Toast.LENGTH_SHORT).show();
            }catch(Exception e){
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Update Error",e.toString());
            }
        }
    }
}
package com.example.newproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.newproject.database.DatabaseHelper;
import com.example.newproject.helper.Expense;
import com.example.newproject.helper.ExpenseAdapter;
import com.example.newproject.helper.ExpenseManager;


public class ListFragment extends Fragment {

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    private View mainView;
    private DatabaseHelper databaseHelper;
    private ListView listView;
    private Spinner sort;
    private Button reverseBtn;
    private boolean reverse;
    private ExpenseAdapter expenseAdapter;
    private ExpenseManager expenseManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 將布局檔案(xml)套用到父容器中(container，通常為創建該Fragment的Activity)
        mainView = inflater.inflate(R.layout.fragment_list, container, false);

        // 獲取Activity的DatabaseHelper
        databaseHelper = ((MainActivity)requireActivity()).getDatabaseHelper();

        expenseManager = databaseHelper.getExpenseManager();



        // 以下為頁面設定



        // 設定Reverse Button
        reverse = true;
        reverseBtn = mainView.findViewById(R.id.reverse);
        reverseBtn.setOnClickListener(v -> {
            reverse = !reverse;
            setListViewAdapter();
        });

        // 設定Sort Spinner的選項
        sort = mainView.findViewById(R.id.selector_in_list);
        ArrayAdapter<CharSequence> sortSelector = ArrayAdapter.createFromResource(
                getContext(), R.array.sort_list, android.R.layout.simple_spinner_item);
        sortSelector.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort.setAdapter(sortSelector);
        sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setListViewAdapter();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 設定listView
        // 要將資料放入ListView中需要透過Adapter放入
        listView = mainView.findViewById(R.id.list_view);
        initListViewAdapter();
        listView.setAdapter(expenseAdapter);

        setOnClickListener();

        return mainView;
    }

    private void setListViewAdapter(){
        databaseHelper.listAllExpenses();
        String sortTag = sort.getSelectedItem().toString();
        switch(sortTag){
            case "COST": expenseManager.sortByCost(reverse);
                        break;
            case "TYPE": expenseManager.sortByType(reverse);
                        break;
            case "DATE": expenseManager.sortByDate(reverse);
                        break;
        }
        expenseAdapter.notifyDataSetChanged();
    }

    private void initListViewAdapter(){
        databaseHelper.listAllExpenses();
        expenseAdapter = new ExpenseAdapter(getContext(), expenseManager.getExpenseList());
    }

    private void setOnClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Expense selectedExpense = (Expense)listView.getItemAtPosition(position);
                ((MainActivity)requireActivity())
                        .replaceFragment(new ExpenseFragment(selectedExpense));
            }
        });
    }

}
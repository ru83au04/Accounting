package com.example.newproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.newproject.database.DatabaseHelper;
import com.example.newproject.helper.Expense;
import com.example.newproject.helper.ExpenseAdapter;
import com.example.newproject.viewmodel.ExpenseViewModel;


public class ListFragment extends Fragment {

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    private View mainView;
    private DatabaseHelper databaseHelper;
    private ListView listView;

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



        // 以下為頁面設定



        // 設定listView
        // 要將資料放入ListView中需要透過Adapter放入
        listView = mainView.findViewById(R.id.list_view);
        setAdapter();

        return mainView;
    }

    /**
     *
     */
    private void setAdapter() {
        ExpenseAdapter expenseAdapter = new ExpenseAdapter(getContext(), Expense.expenseArrayList);
        listView.setAdapter(expenseAdapter);
    }
}
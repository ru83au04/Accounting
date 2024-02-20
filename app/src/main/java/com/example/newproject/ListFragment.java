package com.example.newproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.newproject.database.DatabaseHelper;
import com.example.newproject.helper.Expense;
import com.example.newproject.helper.ExpenseAdapter;
import com.example.newproject.helper.ExpenseManager;
import com.example.newproject.helper.Type;


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
    private View dialogView;
    private AlertDialog dialog;
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

        expenseManager = ((MainActivity)requireActivity()).getExpenseManager();



        // 以下為頁面設定



        // 設定Reverse Button及觸發邏輯
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

        setItemOnClickListener();

        dialogView = getLayoutInflater()
                .inflate(R.layout.dialog_expense, container, false);

        return mainView;
    }

    private void setListViewAdapter(){
        databaseHelper.listAllExpenses();
        String sortTag = sort.getSelectedItem().toString();
        switch(sortTag){
            case "金額": expenseManager.sortByCost(reverse);
                        break;
            case "類型": expenseManager.sortByType(reverse);
                        break;
            case "日期": expenseManager.sortByDate(reverse);
                        break;
        }
        expenseAdapter.notifyDataSetChanged();
    }

    private void initListViewAdapter(){
        if(expenseAdapter == null){
            databaseHelper.listAllExpenses();
            expenseAdapter = new ExpenseAdapter(getContext(), ExpenseManager.expenseList);
        }
        expenseAdapter.notifyDataSetChanged();
    }

    private void setItemOnClickListener() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Expense selectedExpense = (Expense)listView.getItemAtPosition(position);
                if(dialog == null){
                    dialog = new AlertDialog.Builder(getActivity())
                            .setPositiveButton("確認", (d, w) -> {})
                            .setNegativeButton("編輯",(d, w) -> {
                                ((MainActivity)requireActivity()).
                                        replaceFragment(new ExpenseFragment(selectedExpense));
                            }).create();
                }
                dialog.setView(dialogView);

                TextView title = dialogView.findViewById(R.id.dialog_title);
                TextView cost = dialogView.findViewById(R.id.dialog_cost);
                TextView type = dialogView.findViewById(R.id.dialog_type);
                TextView date = dialogView.findViewById(R.id.dialog_date);
                TextView description = dialogView.findViewById(R.id.dialog_description);

                title.setText(selectedExpense.getTitle());
                cost.setText(String.valueOf(selectedExpense.getCost()));
                type.setText(typeToString(selectedExpense.getType()));
                date.setText(dateFormat.format(selectedExpense.getDate()));
                description.setText(selectedExpense.getDescription());
                dialog.show();
            }
        });
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
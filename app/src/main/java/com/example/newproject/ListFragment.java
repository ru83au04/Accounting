package com.example.newproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newproject.database.DatabaseHelper;
import com.example.newproject.helper.Expense;
import com.example.newproject.helper.ExpenseAdapter;
import com.example.newproject.helper.ExpenseManager;
import com.example.newproject.helper.ListAdapter;
import com.example.newproject.helper.Type;


public class ListFragment extends Fragment implements ListAdapter.OnItemClickListener{

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    private View mainView;
    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private Spinner sort;
    private Button reverseBtn;
    private boolean reverse;
    private View dialogView;
    private AlertDialog dialog;
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

        // 獲取Activity的ExpenseManager
        expenseManager = ((MainActivity)requireActivity()).getExpenseManager();



        // 以下為頁面設定


        // 將資料庫的所有帳務讀取至expenseManager的靜態列表
        databaseHelper.listAllExpenses();

        // 設定列表的顯示方式，以LinearLayout的方式去排列
        // 再利用ExpenseManager裡的列表為資料源建立adapter
        // 最後再將兩者綁定在一起
        recyclerView = mainView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        ListAdapter adapter = new ListAdapter(ExpenseManager.expenseList);
        // 將此Fragment作為監聽對象，好處是可以將事件處理邏輯封裝在此Fragment內
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);


        // 設定Reverse Button及觸發邏輯
        reverse = true;
        reverseBtn = mainView.findViewById(R.id.reverse);
        reverseBtn.setOnClickListener(v -> {
            reverse = !reverse;
            setListViewAdapter();
            adapter.notifyDataSetChanged();
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
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 建立共用的AlerDialog視窗
        dialogView = getLayoutInflater()
                .inflate(R.layout.dialog_expense, container, false);

        return mainView;
    }


    /**ListFragment引用了介面ListAdapter.OnItemClickListener
     * 所以要實作抽象方法onItemClick用來作為列表項目被點擊時的動作
     * 在此處的動作狀態為把列表項目的詳細內容以AlertDialog方式顯示出來
     */
    @Override
    public void onItemClick(int position) {
        Expense selectedExpense = ExpenseManager.expenseList.get(position);
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
        date.setText(selectedExpense.getDateByString());
        description.setText(selectedExpense.getDescription());
        dialog.show();
    }

    /**
     * 建立要放入Spinner的選項內容
     */
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
    }

    /**
     * 將enum Type轉為中文字串
     * @param type
     * @return
     */
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
package com.example.newproject.helper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newproject.R;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{
    private final List<Expense> expenseList;
    private OnItemClickListener listener;

    // 用來定義點擊觸發邏輯的介面
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    // 用來設定監聽對象
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // 用來綁定介面與資料的ViewHolder內部類
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, cost, date;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recycler_title);
            cost = itemView.findViewById(R.id.recycler_cost);
            date = itemView.findViewById(R.id.recycler_date);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }

    }

    // 結構式
    public ListAdapter(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    /**
     * 建立ViewHolder
     * 在列表中每個項目都是一個ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_layout, parent, false);
        return new ViewHolder(view);
    }

    /**
     * 每個ViewHolder都綁定一筆資料
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.title.setText(expense.getTitle());
        holder.cost.setText(String.valueOf(expense.getCost()));
        holder.date.setText(expense.getDateByString());
    }

    /**
     * 返回資料的總筆數
     * @return
     */
    @Override
    public int getItemCount() {
        return expenseList.size();
    }
}

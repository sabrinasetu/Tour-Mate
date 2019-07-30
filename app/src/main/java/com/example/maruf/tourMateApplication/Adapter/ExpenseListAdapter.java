package com.example.maruf.tourMateApplication.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.maruf.tourMateApplication.ProjoPackage.Expenses;
import com.example.maruf.tourMateApplication.R;
import java.util.List;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.CustomViewHolder> {
    private Context context;
    private List<Expenses> expensesList;


    public ExpenseListAdapter(Context context, List<Expenses> expensesList){
        this.context = context;
        this.expensesList = expensesList;
    }



    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.expense_recycler_view_list,viewGroup,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int i) {
        Expenses currentExpense = expensesList.get(i);
        holder.details.setText(currentExpense.getDetails());
        holder.amount.setText(String.valueOf(currentExpense.getAmount()));
    }

    @Override
    public int getItemCount() {
        return expensesList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        private TextView amount,details;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.expenseListDetailsTv);
            details = itemView.findViewById(R.id.expenseListNameTv);


        }
    }


}

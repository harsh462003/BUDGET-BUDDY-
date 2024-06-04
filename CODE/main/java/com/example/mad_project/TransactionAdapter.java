package com.example.mad_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> transactionList;

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.typeTextView.setText(transaction.getType());
        holder.amountTextView.setText(transaction.getAmount());
        holder.descriptionTextView.setText(transaction.getDescription());
        holder.dateTextView.setText(transaction.getDate());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView typeTextView;
        public TextView amountTextView;
        public TextView descriptionTextView;
        public TextView dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}

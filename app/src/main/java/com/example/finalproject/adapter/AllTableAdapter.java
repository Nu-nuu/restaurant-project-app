package com.example.finalproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.models.Table;

import java.util.List;

public class AllTableAdapter extends RecyclerView.Adapter<AllTableAdapter.TableViewHolder> {
    private List<Table> tableList;
    private TableClickListener tableClickListener;

    public AllTableAdapter(List<Table> tableList, TableClickListener tableClickListener) {
        this.tableList = tableList;
        this.tableClickListener = tableClickListener;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table_manager, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        Table table = tableList.get(position);
        holder.bind(table);
    }

    @Override
    public int getItemCount() {
        return tableList.size();
    }

    class TableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tableNumberTextView;
        private TextView capacityTextView;
        private TextView statusTextView;
        private ImageButton updateButton;
        private ImageButton deleteButton;

        TableViewHolder(@NonNull View itemView) {
            super(itemView);
            tableNumberTextView = itemView.findViewById(R.id.tableNumberTextView);
            capacityTextView = itemView.findViewById(R.id.capacityTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            itemView.setOnClickListener(this);
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Table table = tableList.get(position);
                        tableClickListener.onUpdateClick(table);
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Table table = tableList.get(position);
                        tableClickListener.onDeleteClick(table);
                    }
                }
            });
        }

        void bind(Table table) {
            tableNumberTextView.setText("Table Number: " + table.getTableNumber());
            capacityTextView.setText("Capacity: " + table.getCapacity());
            statusTextView.setText("Status: " + table.getStatus());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Table table = tableList.get(position);
                tableClickListener.onTableClick(table);
            }
        }
    }

    public interface TableClickListener {
        void onTableClick(Table table);

        void onUpdateClick(Table table);

        void onDeleteClick(Table table);
    }
}

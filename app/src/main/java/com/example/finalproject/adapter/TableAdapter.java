package com.example.finalproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.finalproject.R;
import com.example.finalproject.models.Table;
import java.util.List;
import android.content.Context;
import android.widget.BaseAdapter;

import androidx.core.content.ContextCompat;

public class TableAdapter extends BaseAdapter {
    private Context context;
    private List<Table> tableList;
    private int selectedPosition = -1;

    public TableAdapter(Context context, List<Table> tableList) {
        this.context = context;
        this.tableList = tableList;
    }

    @Override
    public int getCount() {
        return tableList.size();
    }

    @Override
    public Object getItem(int position) {
        return tableList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_table, parent, false);
        }

        TextView tableNumberTextView = convertView.findViewById(R.id.tableNumberTextView);
        TextView capacityTextView = convertView.findViewById(R.id.capacityTextView);

        Table table = tableList.get(position);

        tableNumberTextView.setText("Table Number: "+ String.valueOf(table.getTableNumber()));
        capacityTextView.setText("Capacity: "+ String.valueOf(table.getCapacity()));

        // Apply selection effect to the selected table
        if (position == selectedPosition) {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.selectedTableColor));
        } else {
            convertView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        }

        return convertView;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }
    private void updateTableStatus(String tableId, String status) {
        for (Table table : tableList) {
            if (table.getId().equals(tableId)) {
                table.setStatus(status);
                notifyDataSetChanged();
                break;
            }
        }
    }

}


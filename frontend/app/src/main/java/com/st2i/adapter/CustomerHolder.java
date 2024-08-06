package com.st2i.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.st2i.R;

public class CustomerHolder extends RecyclerView.ViewHolder {

    TextView name,age,numberOfChildren,village;

    public CustomerHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.customerListItem_name);
        age = itemView.findViewById(R.id.customerListItem_age);
        numberOfChildren = itemView.findViewById(R.id.customerListItem_numberOfChildren);
        village = itemView.findViewById(R.id.customerListItem_village);

    }
}

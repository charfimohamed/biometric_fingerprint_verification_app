package com.st2i.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.st2i.R;
import com.st2i.model.Customer;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerHolder> {

    private List<Customer> customerList;

    public CustomerAdapter(List<Customer> customerList) {
        this.customerList = customerList;
    }

    @NonNull
    @Override
    public CustomerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_customer_item, parent,false);
        return new CustomerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerHolder holder, int position) {
        Customer customer = customerList.get(position);
        //modify here to change the text ie. add age: or number of children:
        holder.name.setText(customer.getName());
        holder.age.setText("age: "+String.valueOf(customer.getAge()));
        holder.numberOfChildren.setText("number of children: "+String.valueOf(customer.getNumberOfChildren()));
        holder.village.setText(customer.getVillage());

    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }
}

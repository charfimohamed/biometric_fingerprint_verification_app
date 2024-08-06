package com.st2i;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.st2i.adapter.CustomerAdapter;
import com.st2i.model.Customer;
import com.st2i.retrofit.CustomerApi;
import com.st2i.retrofit.RetrofitService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class customerListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);
        recyclerView = findViewById(R.id.customerList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton floatingActionButton = findViewById(R.id.customerListFAB);
        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, customerFormActivity.class);
            startActivity(intent);
                });
        
        loadCustomers();
    }

    private void loadCustomers() {
        RetrofitService retrofitService = new RetrofitService();
        CustomerApi customerApi = retrofitService.getRetrofit().create(CustomerApi.class);
        customerApi.getCustomers()
                .enqueue(new Callback<List<Customer>>() {
                    @Override
                    public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                        populateListView(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Customer>> call, Throwable t) {
                        Toast.makeText(customerListActivity.this, "failed to load the customers", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(customerFormActivity.class.getName()).log(Level.SEVERE, "failed to load the customers", t);
                    }
                });
    }

    private void populateListView(List<Customer> customerList) {
        CustomerAdapter customerAdapter = new CustomerAdapter(customerList);
        recyclerView.setAdapter(customerAdapter);
    }
}
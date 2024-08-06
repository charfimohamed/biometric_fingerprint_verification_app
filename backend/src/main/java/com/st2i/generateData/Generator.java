package com.st2i.generateData;


import com.st2i.model.Customer;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.st2i.retrofit.CustomerApi;
import com.st2i.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Generator {
    private static void generateCustomers(){
        Set<String> uniqueFingerprints = new HashSet<>();

        for (int i = 0; i < 2000; i++) {
            byte[] fingerprint;
            do {
                fingerprint = generateRandomFingerprint();
            } while (!uniqueFingerprints.add(Arrays.toString(fingerprint)));

            // Create a random customer with the unique fingerprint
            Customer randomCustomer = new Customer();
            // Set other customer attributes here as needed
            randomCustomer.setFingerprint(fingerprint);
            randomCustomer.setAge(2);
            randomCustomer.setName("Hamma");
            randomCustomer.setVillage("Tunis");
            randomCustomer.setNumberOfChildren(0);

            RetrofitService retrofitService = new RetrofitService();
            CustomerApi customerApi = retrofitService.getRetrofit().create(CustomerApi.class);

            customerApi.addCustomer(randomCustomer).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                }
            });


        }

    }

    private static byte[] generateRandomFingerprint() {
        byte[] fingerprint = new byte[396]; // Change the size as per your fingerprint data size
        new SecureRandom().nextBytes(fingerprint);
        return fingerprint;
    }

    public static void main(String[] args) {
        generateCustomers();
    }
}

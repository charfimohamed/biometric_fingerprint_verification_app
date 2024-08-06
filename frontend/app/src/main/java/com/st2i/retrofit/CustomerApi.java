package com.st2i.retrofit;

import com.st2i.model.Customer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CustomerApi {
    @GET("/api/v1/customers")
    Call<List<Customer>> getCustomers() ;

    @POST("/api/v1/customers")
    Call<Void> addCustomer(@Body Customer customer) ;

    @DELETE("/api/v1/customers/{customerId}")
    Call<Void> deleteCustomer(@Path("customerId") Integer id);

    @PUT("/api/v1/customers/{customerId}")
    Call<Void> updateCustomer(@Path("customerId") Integer id,@Body Customer customer);
}

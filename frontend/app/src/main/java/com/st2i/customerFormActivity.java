package com.st2i;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.st2i.model.Customer;
import com.st2i.retrofit.CustomerApi;
import com.st2i.retrofit.RetrofitService;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.suprema.BioMiniFactory;
import com.suprema.CaptureResponder;
import com.suprema.IBioMiniDevice;
public class customerFormActivity extends AppCompatActivity {

    private byte[] capturedFingerprint;
    private static BioMiniFactory mBioMiniFactory = null;
    private IBioMiniDevice mCurrentDevice;

    private List<Customer> customerList;


    //here

    private IBioMiniDevice.CaptureOption mCaptureOptionDefault = new IBioMiniDevice.CaptureOption();
    private CaptureResponder mCaptureResponseDefault = new CaptureResponder() {
        @Override
        public boolean onCaptureEx(final Object context, final Bitmap capturedImage,
                                   final IBioMiniDevice.TemplateData capturedTemplate,
                                   final IBioMiniDevice.FingerState fingerState) {
            System.out.println("onCapture : Capture successful!");
            System.out.println(((IBioMiniDevice) context).popPerformanceLog());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(capturedImage != null) {
                        ImageView iv = (ImageView) findViewById(R.id.imagePreview);
                        if(iv != null) {
                            iv.setImageBitmap(capturedImage);
                        }
                    }
                }
            });
            return true;
        }

        @Override
        public void onCaptureError(Object contest, int errorCode, String error) {
            System.out.println("onCaptureError : " + error + " ErrorCode :" + errorCode);
        }
    };

    // here


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
    }

    private void initializeComponents() {
        initializeSensor();
        TextInputEditText inputEditName = findViewById(R.id.formTextFieldName);
        TextInputEditText inputEditVillage = findViewById(R.id.formTextFieldVillage);
        TextInputEditText inputEditAge = findViewById(R.id.formTextFieldAge);
        TextInputEditText inputEditNumberOfChildren = findViewById(R.id.formTextFieldNumberOfChildren);
        MaterialButton addButton = findViewById(R.id.formAddButton);
        MaterialButton addFingerprint = findViewById(R.id.formAddFingerprint);


        // capturing fingerprint
        addFingerprint.setOnClickListener(v -> {
            IBioMiniDevice.CaptureOption option = new IBioMiniDevice.CaptureOption();
            option.extractParam.captureTemplate = true;
            option.captureTemplate = true;
            mCurrentDevice.captureSingle(option,
                    new CaptureResponder() {
                        @Override
                        public boolean onCaptureEx(final Object context, final Bitmap capturedImage,
                                                   final IBioMiniDevice.TemplateData capturedTemplate,
                                                   final IBioMiniDevice.FingerState fingerState) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(capturedImage != null) {
                                        ImageView iv = (ImageView) findViewById(R.id.imagePreview);
                                        if(iv != null) {
                                            iv.setImageBitmap(capturedImage);
                                        }
                                    }
                                }
                            });
                            if(capturedTemplate != null) {
                                loadCustomers();
                                if(customerList!= null){
                                    for (Customer oldCustomer : customerList) {
                                        if (mCurrentDevice.verify(capturedTemplate.data,capturedTemplate.data.length,oldCustomer.getFingerprint(),oldCustomer.getFingerprint().length))
                                        {
                                            // Inside your background thread within an Activity or Fragment
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    StyleableToast.makeText(customerFormActivity.this,"The customer's fingerprint is present in the database !",R.style.presentInDatabaseToast).show();
                                                }
                                            });
                                            return false;
                                        }
                                    }

                                    capturedFingerprint = capturedTemplate.data;


                                }
                                else {
                                    capturedFingerprint = capturedTemplate.data;
                                }


                            }

                            return true;
                        }

                        @Override
                        public void onCaptureError(Object context, int errorCode, String error) {
                            Toast.makeText(customerFormActivity.this, "onCaptureError : " + error, Toast.LENGTH_SHORT).show();
                        }
                    }, true);});

        // sending the information to the database
        RetrofitService retrofitService = new RetrofitService();
        CustomerApi customerApi = retrofitService.getRetrofit().create(CustomerApi.class);


        addButton.setOnClickListener(view -> {
            if(areAllFieldsFilled())
            {
            String name = String.valueOf(inputEditName.getText());
            String village = String.valueOf(inputEditVillage.getText());
            Integer age = Integer.valueOf(String.valueOf(inputEditAge.getText()));
            Integer numberOfChildren = Integer.valueOf(String.valueOf(inputEditNumberOfChildren.getText()));
            Customer customer = new Customer();
            customer.setName(name);
            customer.setVillage(village);
            customer.setAge(age);
            customer.setNumberOfChildren(numberOfChildren);
            customer.setFingerprint(capturedFingerprint);

            customerApi.addCustomer(customer).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(customerFormActivity.this, "ADDING THE CUSTOMER IS SUCCESSFUL", Toast.LENGTH_SHORT).show();
                    resetForm();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(customerFormActivity.this, "ADDING THE CUSTOMER FAILED!", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
                Toast.makeText(this, "NOT ALL THE FIELDS ARE VALID", Toast.LENGTH_SHORT).show();
            }});
    }

    private void initializeSensor() {

        mBioMiniFactory = new BioMiniFactory(this) {
            @Override
            public void onDeviceChange(DeviceChangeEvent event, Object dev) {
                if (event == DeviceChangeEvent.DEVICE_ATTACHED && mCurrentDevice == null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (mBioMiniFactory != null) {
                                // find BioMini device, get user permission and SDK initialize
                                mCurrentDevice = mBioMiniFactory.getDevice(0);
                            }
                        }
                    }).start();
                }
            }

        };
    }


    private void loadCustomers() {
        RetrofitService retrofitService = new RetrofitService();
        CustomerApi customerApi = retrofitService.getRetrofit().create(CustomerApi.class);
        customerApi.getCustomers()
                .enqueue(new Callback<List<Customer>>() {
                    @Override
                    public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                        customerList = response.body();
                        //if (customerList.isEmpty()) {
                        //    customerList = null;
                        //    //Toast.makeText(customerFormActivity.this, "customerList is null", Toast.LENGTH_SHORT).show();
                        //}
                    }

                    @Override
                    public void onFailure(Call<List<Customer>> call, Throwable t) {
                        Toast.makeText(customerFormActivity.this, "failed to load customer", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(customerFormActivity.class.getName()).log(Level.SEVERE, "failed to load the customers", t);
                    }
                });
    }


    private boolean areAllFieldsFilled() {
        TextInputEditText inputEditName = findViewById(R.id.formTextFieldName);
        TextInputEditText inputEditVillage = findViewById(R.id.formTextFieldVillage);
        TextInputEditText inputEditAge = findViewById(R.id.formTextFieldAge);
        TextInputEditText inputEditNumberOfChildren = findViewById(R.id.formTextFieldNumberOfChildren);

        String name = String.valueOf(inputEditName.getText());
        String village = String.valueOf(inputEditVillage.getText());
        String age = String.valueOf(inputEditAge.getText());
        String numberOfChildren = String.valueOf(inputEditNumberOfChildren.getText());
        return !name.isEmpty() && !village.isEmpty() && !age.isEmpty() && !numberOfChildren.isEmpty() && capturedFingerprint!=null;

    }


    private void resetForm() {
        TextInputEditText inputEditName = findViewById(R.id.formTextFieldName);
        TextInputEditText inputEditVillage = findViewById(R.id.formTextFieldVillage);
        TextInputEditText inputEditAge = findViewById(R.id.formTextFieldAge);
        TextInputEditText inputEditNumberOfChildren = findViewById(R.id.formTextFieldNumberOfChildren);
        ImageView imagePreview = findViewById(R.id.imagePreview);

        inputEditName.setText("");
        inputEditVillage.setText("");
        inputEditAge.setText("");
        inputEditNumberOfChildren.setText("");
        imagePreview.setImageDrawable(null);
        capturedFingerprint = null;
    }








}

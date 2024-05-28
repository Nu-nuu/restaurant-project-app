package com.example.finalproject.activity.member;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;
import com.example.finalproject.api.ApiService;
import com.example.finalproject.api.CreateOrder;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class ZaloPayActivity extends AppCompatActivity {
    private ApiService apiService;
    TextView lblZpTransToken, txtToken;
    Button btnCreateOrder, btnPay, btnMomo, btnVisaCard;
    TextView txtAmount;

    private void BindView() {
        txtToken = findViewById(R.id.txtToken);
        lblZpTransToken = findViewById(R.id.lblZpTransToken);
        btnCreateOrder = findViewById(R.id.btnCreateOrder);
        txtAmount = findViewById(R.id.txtAmount);
        btnPay = findViewById(R.id.btnPay);
        btnMomo = findViewById(R.id.btnMomo);
        btnVisaCard = findViewById(R.id.btnVisaCard);
        IsLoading();
    }

    private void IsLoading() {
        lblZpTransToken.setVisibility(View.INVISIBLE);
        txtToken.setVisibility(View.INVISIBLE);
        btnPay.setVisibility(View.INVISIBLE);
    }

    private void IsDone() {
        lblZpTransToken.setVisibility(View.INVISIBLE);
        txtToken.setVisibility(View.INVISIBLE);
        btnPay.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zalo_pay_activity);
        apiService = ApiService.createApiService();
        String reservationId = getIntent().getStringExtra("reservationId");
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        // bind components with ids
        BindView();
        // handle CreateOrder
        btnCreateOrder.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                CreateOrder orderApi = new CreateOrder();

                try {
                    JSONObject data = orderApi.createOrder(txtAmount.getText().toString());

                    lblZpTransToken.setVisibility(View.INVISIBLE);
                    String code = data.getString("return_code");

                    if (code.equals("1")) {
                        lblZpTransToken.setText("zptranstoken");
                        txtToken.setText(data.getString("zp_trans_token"));
                        IsDone();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = txtToken.getText().toString();
                ZaloPaySDK.getInstance().payOrder(ZaloPayActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Start SuccessfullyActivity
                                Intent intent = new Intent(ZaloPayActivity.this, SuccessfullyActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        });
                        IsLoading();
                    }

                    @Override
                    public void onPaymentCanceled(String zpTransToken, String appTransID) {
                        showPaymentCancelDialog(zpTransToken);
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(ZaloPayActivity.this, ErrorActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });
            }
        });
        btnMomo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(ZaloPayActivity.this)
                        .setTitle("Momo Wallet Selected")
                        .setMessage("Momo Wallet payment is not available at the moment.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Handle positive button click if needed
                            }
                        })
                        .create();
                alertDialog.show();
            }
        });
        btnVisaCard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(ZaloPayActivity.this)
                        .setTitle("Visa Card Selected")
                        .setMessage("Visa Card payment is not available at the moment.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Handle positive button click if needed
                            }
                        })
                        .create();
                alertDialog.show();
            }
        });
        Button cancelButton = findViewById(R.id.btnCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelDialog();
            }
        });

    }

    private void cancelReservation() {
        String reservationId = getIntent().getStringExtra("reservationId");
        Call<Void> call = apiService.deleteReservation(reservationId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    Intent intent = new Intent(ZaloPayActivity.this, MemberHomePageActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("cancelReservation", "Failed to cancel Reservation: " + response.message());
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("cancelReservation", "Error cancel Reservation: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }


    private void showPaymentCancelDialog(String zpTransToken) {
        AlertDialog alertDialog = new AlertDialog.Builder(ZaloPayActivity.this)
                .setTitle("User Cancel Payment")
                .setMessage("You cancelled, please choose ZaloPay again to Pay now.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        alertDialog.show();
    }
    private void showCancelDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(ZaloPayActivity.this)
                .setTitle("Confirm Cancel")
                .setMessage("Do you want to cancel?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelReservation();
                    }
                })
                .setNegativeButton("No", null)
                .create();
        alertDialog.show();
    }

}

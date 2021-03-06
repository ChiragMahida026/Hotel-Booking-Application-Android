package com.example.hotelbookingsystem;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.Session;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelbookingsystem.news.roomcartFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.razorpay.Checkout;
import com.razorpay.CheckoutActivity;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private Button buttonConfirmOrder;
    private EditText editTextPayment;
    EditText usermail,userphone;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userId;
    String userphones,emailsss,payment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();


        findViews();
        listeners();
    }

    public void findViews() {
        buttonConfirmOrder = (Button) findViewById(R.id.bt_pay);
        editTextPayment = (EditText) findViewById(R.id.editTextPayment);
        usermail=findViewById(R.id.usermail);
        userphone=findViewById(R.id.userphone);
        userId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fstore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                userphone.setText(value.getString("phone"));
                Toast.makeText(getApplicationContext(), userphone.getText().toString(), Toast.LENGTH_SHORT).show();
                usermail.setText(value.getString("email"));
                Toast.makeText(getApplicationContext(), usermail.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        editTextPayment.setText(roomcartFragment.getValues());


    }

    public void listeners() {


        buttonConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextPayment.getText().toString().equals(""))
                {
                    Toast.makeText(PaymentActivity.this, "Please fill payment", Toast.LENGTH_LONG).show();
                    return;
                }
                startPayment();
            }
        });
    }

    private void startPayment() {

        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Demoing Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");

             payment = editTextPayment.getText().toString();

            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();

            userphones=userphone.getText().toString();
            emailsss=usermail.getText().toString();

            preFill.put("email", emailsss);
            preFill.put("contact",userphones );
            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {

        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        Toast.makeText(this, "Payment successfully done! ", Toast.LENGTH_SHORT).show();


                    DocumentReference documentReference = fstore.collection("Payments").document(s);
                    Map<String, Object> payments = new HashMap<>();
                    payments.put("PaymentId",s);
                    payments.put("Payment",payment);
                    payments.put("emails",emailsss);
                    payments.put("name",userphones);
//                    payments.put("usernamesss",fAuth.getCurrentUser().getUid());
                    documentReference.set(payments);
    }

    @Override
    public void onPaymentError(int i, String s) {

        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {


        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }
}